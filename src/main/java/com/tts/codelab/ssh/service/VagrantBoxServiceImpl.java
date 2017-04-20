package com.tts.codelab.ssh.service;

import com.tts.codelab.ssh.domain.VagrantBoxSession;
import com.tts.codelab.ssh.repository.VagrantBoxSessionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class VagrantBoxServiceImpl implements VagrantBoxService {

    @Autowired
    private SSHServerService sshServerService;

    @Autowired
    private VagrantBoxSessionRepository repository;

    private Map<String, VagrantBoxSession> vagrantBoxSessions = new ConcurrentHashMap<>();
    
    @PostConstruct
    public void initialize(){
        // Loading current provisioned vagrant box session from database. Which is for the case that
        //      we reboot application but vagrant box sessions are still there.
        repository.findAll().forEach(vagrantSession -> {
            vagrantBoxSessions.put(vagrantSession.getSessionId(), vagrantSession);
        });
    }

    @Override
    public VagrantBoxSession provision(String owner, String boxName, int ram) {
        return null;
    }

    @Override
    public void destroy(String sessionId) {

    }
}
