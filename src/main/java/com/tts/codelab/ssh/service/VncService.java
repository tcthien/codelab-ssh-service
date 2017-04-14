package com.tts.codelab.ssh.service;

import com.tts.codelab.ssh.domain.UserVncSession;
import com.tts.codelab.ssh.domain.VncSessionSummary;

public interface VncService {
    UserVncSession findBySessionId(String sessionId);

    VncSessionSummary findSummary();

    UserVncSession startNewVncSession(UserVncSession vncSession);

    UserVncSession stopBySessionId(String sessionId);
}
