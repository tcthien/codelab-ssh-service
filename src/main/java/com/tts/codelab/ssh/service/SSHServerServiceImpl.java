package com.tts.codelab.ssh.service;

import com.tts.codelab.ssh.domain.SSHServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class SSHServerServiceImpl implements SSHServerService {
    @Value("${codelab.config.sshServer}")
    private List<String> sshServerConfigurations;

    private Set<SSHServer> sshServers = new HashSet<>();

    @PostConstruct
    public void initialize() {
        if (sshServerConfigurations != null && !sshServerConfigurations.isEmpty()) {
            sshServerConfigurations.forEach(sshServer -> {
                String[] arr = sshServer.split(",");
                sshServers.add(SSHServer.builder().serverIp(arr[0]).userName(arr[1]).password(arr[2]).build());
            });
        }
    }
}