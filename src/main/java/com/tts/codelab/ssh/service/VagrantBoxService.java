package com.tts.codelab.ssh.service;

import com.tts.codelab.ssh.domain.VagrantBoxSession;

/**
 * Created by tcthien on 4/20/2017.
 */
public interface VagrantBoxService {
    public VagrantBoxSession provision(String owner, String boxName, int ram);

    public void destroy(String sessionId);
}
