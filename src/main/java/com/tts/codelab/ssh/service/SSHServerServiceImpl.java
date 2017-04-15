package com.tts.codelab.ssh.service;

import com.tts.codelab.ssh.domain.SSHServer;
import com.tts.codelab.ssh.ssh.execute.SSHCommandExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private SSHCommandExecutor sshCommandExecutor;

    @PostConstruct
    public void initialize() {
        if (sshServerConfigurations != null && !sshServerConfigurations.isEmpty()) {
            sshServerConfigurations.forEach(sshServer -> {
                String[] arr = sshServer.split(",");
                String serverIp = null;
                int port = 22;
                int idx = arr[0].indexOf(":");
                if (idx >= 0) {
                    String[] tmp = arr[0].split("\\:");
                    serverIp = tmp[0];
                    port = Integer.parseInt(tmp[1]);
                }
                sshServers.add(SSHServer.builder().serverIp(serverIp).port(port).userName(arr[1]).password(arr[2]).build());
            });
        }
    }
}