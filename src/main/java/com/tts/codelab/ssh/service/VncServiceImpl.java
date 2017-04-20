package com.tts.codelab.ssh.service;

import com.tts.codelab.ssh.domain.UserVncSession;
import com.tts.codelab.ssh.domain.VncSessionSummary;
import com.tts.codelab.ssh.repository.VncSessionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VncServiceImpl implements VncService {

    @Autowired
    private SSHServerService sshServerService;

    @Autowired
    private VncSessionRepository repository;

    @Autowired
    private VagrantBoxService vagrantBoxService;

    @Override
    public UserVncSession findBySessionId(String sessionId) {
        return null;
    }

    @Override
    public VncSessionSummary findSummary() {
        return null;
    }

    @Override
    public UserVncSession startNewVncSession(UserVncSession vncSession) {
        // SSH to main server to check & provision new vagrant box session
        // SSH to vagrant box server with default SSH password & update specific password for user
        // SSH to vagrant box & startVNC Session
        return null;
    }

    @Override
    public UserVncSession stopBySessionId(String sessionId) {
        return null;
    }
}
