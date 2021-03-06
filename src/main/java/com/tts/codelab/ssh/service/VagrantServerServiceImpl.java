package com.tts.codelab.ssh.service;

import com.tts.codelab.ssh.domain.VagrantServer;
import com.tts.codelab.ssh.ssh.execute.SSHCommandExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
@Slf4j
public class VagrantServerServiceImpl implements VagrantServerService {

    public static final String CODELAB_CONFIG_SSH_SERVER = "codelab.config.sshServer";
    @Autowired
    private Environment env;

    @Value("${codelab.config.servers}")
    private List<String> sshServerConfigurations;

    private Map<String, VagrantServer> vagrantServersByIp = new HashMap<>();

    @Autowired
    private SSHCommandExecutor sshCommandExecutor;

    @PostConstruct
    public void initialize() {
        if (sshServerConfigurations != null && !sshServerConfigurations.isEmpty()) {
            sshServerConfigurations.forEach(serverName -> {
                String serverIp = env.getProperty(CODELAB_CONFIG_SSH_SERVER + "." + serverName + ".ip");
                String port = env.getProperty(CODELAB_CONFIG_SSH_SERVER + "." + serverName + ".port");
                String userName = env.getProperty(CODELAB_CONFIG_SSH_SERVER + "." + serverName + ".userName");
                String password = env.getProperty(CODELAB_CONFIG_SSH_SERVER + "." + serverName + ".password");
                String vagrantRoot = env.getProperty(CODELAB_CONFIG_SSH_SERVER + "." + serverName + ".vagrantRoot");
                List<String> vagrantSubFolder = env.getProperty(CODELAB_CONFIG_SSH_SERVER + "." + serverName + "" +
                        ".vagrantSubFolder", List.class);
                Map<String, Integer> vagrantSubMap = new HashMap<>();
                vagrantSubFolder.forEach(subFolder -> {
                    String[] arr = subFolder.split("\\:");
                    vagrantSubMap.put(arr[0], Integer.valueOf(arr[1]));
                });

                vagrantServersByIp.put(serverIp, VagrantServer.builder().serverIp(serverIp).port(Integer.valueOf(port))
                        .userName(userName).password(password).vagrantRootFolder(vagrantRoot)
                        .vagrantSubFolder(vagrantSubMap).build());
            });
        }
    }

    @Override
    public Collection<VagrantServer> getVagrantServers() {
        return vagrantServersByIp.values();
    }

    @Override
    public VagrantServer getVagrantServer(String serverIp) {
        return vagrantServersByIp.get(serverIp);
    }
}