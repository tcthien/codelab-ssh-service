package com.tts.codelab.ssh.service;

import com.tts.codelab.ssh.domain.VagrantServer;

import java.util.List;

public interface VagrantServerService {
    List<VagrantServer> getAvailableVagrantServer();
}
