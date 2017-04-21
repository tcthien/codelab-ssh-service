package com.tts.codelab.ssh.service;

import com.tts.codelab.ssh.domain.VagrantServer;

import java.util.Collection;

public interface VagrantServerService {
    Collection<VagrantServer> getVagrantServers();

    VagrantServer getVagrantServer(String serverIp);
}
