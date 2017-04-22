package com.tts.codelab.ssh.service;

import com.tts.codelab.ssh.domain.VagrantBoxSession;
import com.tts.codelab.ssh.domain.VagrantServer;
import com.tts.codelab.ssh.domain.VncSessionSummary;
import com.tts.codelab.ssh.exception.UnavailableVagrantServerException;
import com.tts.codelab.ssh.repository.VagrantBoxSessionRepository;
import com.tts.codelab.ssh.ssh.execute.SSHCommandExecutor;
import com.tts.codelab.ssh.ssh.vagrant.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class VagrantBoxServiceImpl implements VagrantBoxService {

    public static final int VAGRANT_BOX_BASE_PORT = 30000;
    @Autowired
    private VagrantServerService vagrantServerService;

    @Autowired
    private VagrantBoxSessionRepository repository;

    @Autowired
    private SSHCommandExecutor executor;

    // Map<Session ID, Vagrant Box Session>
    private Map<String, VagrantBoxSession> vagrantBoxBySessionId = new ConcurrentHashMap<>();

    private Object lock = new Object();

    @PostConstruct
    public void initialize() {
        // Loading current provisioned vagrant box session from database. Which is for the case that
        //      we reboot application but vagrant box sessions are still there.
        repository.findAll().forEach(vagrantSession -> {
            vagrantBoxBySessionId.put(vagrantSession.getSessionId(), vagrantSession);
        });
    }

    // Schedule task every 5mins to check & clean up vagrant box session
    @Scheduled(fixedDelay = 300000)
    public void cleanVagrantBoxSession() {
        List<VagrantBoxSession> lst = new ArrayList<>();
        synchronized (lock){
            // Synchronize to make sure the consistency in multi thread context
            lst.addAll(vagrantBoxBySessionId.values());
        }
        // Do in parallel for better better performance
        lst.parallelStream().forEach(session -> {
            Date startTime = session.getProvisionTime();
            Date stopTime = new Date(startTime.getTime());
            stopTime.setMinutes(startTime.getMinutes() + 30); // Stop after 30mins provisioning
            Date currentTime = new Date();
            if (currentTime.after(stopTime)) {
                try {
                    destroy(session.getSessionId());
                } catch (Exception e) {
                    log.error("Failed to destroy " + session.getSessionId(), e);
                }
            }
        });
    }

    /**
     * At the moment, there are two important ports: SSH (22) & VNC (5901) and one server may server many vagrant
     * box sessions, so the idea is we will define port forwarding (from host to vagrant box) with following rules:
     * - 30000: This is base port for vagrant session
     * - For every vagrant session, we will base on id to generate port forwarding.
     * For example with id=1
     * + SSH Port: 30000 + (1 * 100) + 22 = 30122
     * + VNC Port: 30000 + (1 * 100) + 1 = 30101   // 1 here is VNC Session ID
     * <p>
     * For example with id=2
     * + SSH Port: 30000 + (2 * 100) + 22 = 30222
     * + VNC Port: 30000 + (2 * 100) + 1 = 30201   // 1 here is VNC Session ID
     */
    @Override
    public VagrantBoxSession provision(String owner, String boxName, int memory) throws Exception {
        log.info("Provisioning: {" + boxName + ", " + memory + " MB, " + owner + "}");
        try{
            // Check if user already provision a vagrant box session -------------------------------------------------
            for (VagrantBoxSession session : vagrantBoxBySessionId.values()) {
                if (owner.equals(session.getOwner())) {
                    return session;
                }
            }

            // Provision a new Vagrant Box Session -------------------------------------------------------------------
            return provisionNewVagrantBoxSession(owner, boxName, memory);
        } finally {
            log.info("Provisioning: {" + boxName + ", " + memory + " MB, " + owner + "} completed");
        }
    }

    private VagrantBoxSession provisionNewVagrantBoxSession(String owner, String boxName, int memory) throws Exception {
        VagrantSubInfo vagrantSubInfo = getAvailableVagrantSubFolder();

        String vagrantSubFolder = vagrantSubInfo.getVagrantSubFolder();
        String serverIp = vagrantSubInfo.getServerIp();

        if (vagrantSubInfo == null) {
            throw new UnavailableVagrantServerException();
        }

        // Calculate important ports: SSH & VNC
        int id = vagrantSubInfo.getVagrantId();
        int vagrantSSHPort = VAGRANT_BOX_BASE_PORT + (id * 100) + 22;
        int vagrantVncPort = VAGRANT_BOX_BASE_PORT + (id * 100) + 1;

        // At present we are hardcoding the fix value of RAM, Box Type, Box Name in the Vagrantfile placed under
        // /vagrant
        String sessionId = UUID.randomUUID().toString();

        // Calculate new password basing on sessionId
        StringBuilder vagrantSSHPass = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            vagrantSSHPass.append(sessionId.charAt(i));
        }

        VagrantBoxSession session = VagrantBoxSession.builder().sessionId(sessionId).boxName(boxName).memory(memory)
                .sshPort(vagrantSSHPort).vncPort(vagrantVncPort).owner(owner).serverIp(serverIp)
                .vagrantSubFolder(vagrantSubFolder).build();

        log.info("Provisioning session " + sessionId);
        log.debug("    " + session);
        // Put to map
        vagrantBoxBySessionId.put(sessionId, session);
        // Save to database
        repository.save(session);

        VagrantServer vagrantServer = vagrantServerService.getVagrantServer(serverIp);
        // variable for SSH executing
        String host = serverIp;
        int serverSSHPort = vagrantServer.getPort();
        String userName = vagrantServer.getUserName();
        String password = vagrantServer.getPassword();

        String vagrantUser = "vagrant";

        // Start Vagrant Session on Server
        new VagrantUpCommand(session.getVagrantSubFolder(), session.getSessionId()).execute(executor, host,
                serverSSHPort, userName, password); // Usually is 22
        // Change Vagrant box password on Vagrant Box Session
        new ChangePasswordCommand(vagrantUser, vagrantSSHPass.toString()).execute(executor, host, vagrantSSHPort,
                vagrantUser, vagrantUser); // By default username & password is vagrant so we need to change password
        // Change VNC password on Vagrant Box Session
        new ChangeVNCPasswordCommand(vagrantSSHPass.toString()).execute(executor, host, vagrantSSHPort,
                vagrantUser, vagrantSSHPass.toString());
        // Start VNC on Vagrant Box Session
        new StartVNCCommand(1).execute(executor, host, vagrantSSHPort,
                vagrantUser, vagrantSSHPass.toString());
        return session;
    }

    @Override
    public void destroy(String sessionId) throws Exception {
        log.info("Destroying: {" + sessionId + "}");
        try{
            VagrantBoxSession session = vagrantBoxBySessionId.get(sessionId);
            if (session == null) {
                return;
            }

            VagrantServer vagrantServer = vagrantServerService.getVagrantServer(session.getServerIp());
            if (vagrantServer == null) {
                return;
            }

            // variable for SSH executing
            String host = vagrantServer.getServerIp();
            int serverSSHPort = vagrantServer.getPort();
            String userName = vagrantServer.getUserName();
            String password = vagrantServer.getPassword();

            try {
                // Start Vagrant Session on Server
                new VagrantDestroyCommand(session.getVagrantSubFolder()).execute(executor,
                        host, serverSSHPort, userName, password); // Usually is 22
            } finally {
                // Remove out of the map
                vagrantBoxBySessionId.remove(sessionId);
                // Delete in database
                repository.delete(sessionId);
            }
        } finally {
            log.info("Destroying: {" + sessionId + "} completed");
        }
    }

    @Override
    public VagrantBoxSession getVagrantBoxSession(String sessionId) {
        return vagrantBoxBySessionId.get(sessionId);
    }

    @Override
    public VncSessionSummary getVagrantBoxSummary() {
        int runningSession = vagrantBoxBySessionId.size();
        int availableSession = getNumberOfAvailableVagrantSession();
        return VncSessionSummary.builder().numberOfAvailableSession(availableSession).numberOfRunningSession
                (runningSession).build();
    }

    @Builder
    @Getter
    @Setter
    @Data
    private static class VagrantSubInfo {
        private String serverIp;
        private String vagrantSubFolder;
        private Integer vagrantId;
    }

    private static interface AvailableSessionCounter {
        void increase(int count);
    }

    private int getNumberOfAvailableVagrantSession() {
        int[] counter = new int[]{0};
        getAvailableVagrantSubFolder(count -> {
            counter[0] += count;
        });
        return counter[0];
    }

    private VagrantSubInfo getAvailableVagrantSubFolder() {
        return getAvailableVagrantSubFolder(null);
    }

    /**
     * @return Entry<Vagrant Sub Folder, Vagrant ID>: for example
     * + Vagrant Sub Folder: <Vagrant Root Folder>/user1, <Vagrant Root Folder>/user2
     * + Vagrant ID: 1, 2, 3... This number will be used to calculate port mapping
     */
    private VagrantSubInfo getAvailableVagrantSubFolder(AvailableSessionCounter counter) {
        Collection<VagrantServer> vagrantServers = vagrantServerService.getVagrantServers();
        if (vagrantServers == null || vagrantServers.isEmpty()) {
            throw new UnavailableVagrantServerException();
        }
        VagrantSubInfo vagrantSubInfo = null;

        for (VagrantServer vagrantServer : vagrantServers) {
            Map<String, Integer> vagrantSubFolders = vagrantServer.getVagrantSubFolder();
            String serverIp = vagrantServer.getServerIp();

            Set<VagrantBoxSession> runningSessions = new HashSet<>();
            vagrantBoxBySessionId.values().forEach(session -> {
                if (serverIp.equalsIgnoreCase(session.getServerIp())) {
                    runningSessions.add(session);
                }
            });

            // There is no Vagrant Session running => get any
            if (runningSessions == null || runningSessions.isEmpty()) {
                if (counter != null) {
                    counter.increase(vagrantSubFolders.size());
                } else {
                    Map.Entry<String, Integer> runningSession = vagrantSubFolders.entrySet().iterator()
                            .next();
                    vagrantSubInfo = VagrantSubInfo.builder().serverIp(serverIp)
                            .vagrantSubFolder(runningSession.getKey()).vagrantId(runningSession.getValue()).build();
                    break;
                }
            }

            // There are some Vagrant session running => get the new one
            // Build up running set for fast checking
            Set<String> vagrantSubRunning = new HashSet<>();
            runningSessions.forEach(runningSession -> {
                vagrantSubRunning.add(runningSession.getVagrantSubFolder());
            });

            for (Map.Entry<String, Integer> entry : vagrantSubFolders.entrySet()) {
                String vagrantSubFolder = entry.getKey();
                Integer vagrantId = entry.getValue();

                if (!vagrantSubRunning.contains(vagrantSubFolder)) {
                    if (counter != null) {
                        counter.increase(1);
                    } else {
                        // This vagrant sub session is not provisioned -> add to Entry for starting
                        vagrantSubInfo = VagrantSubInfo.builder().serverIp(serverIp)
                                .vagrantSubFolder(vagrantSubFolder).vagrantId(vagrantId).build();
                        break;
                    }
                }
            }

        }
        return vagrantSubInfo;
    }
}
