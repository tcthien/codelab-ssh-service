package com.tts.codelab.ssh.service;

import com.tts.codelab.ssh.domain.VagrantBoxSession;
import com.tts.codelab.ssh.exception.UnavailableVagrantServerException;

/**
 * Created by tcthien on 4/20/2017.
 */
public interface VagrantBoxService {
    public VagrantBoxSession provision(String owner, String boxName, int ram) throws Exception;

    public void destroy(String sessionId) throws Exception;

    VagrantBoxSession getVagrantBoxSession(String sessionId);
}
