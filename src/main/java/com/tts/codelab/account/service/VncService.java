package com.tts.codelab.account.service;

import com.tts.codelab.account.domain.UserVncSession;
import com.tts.codelab.account.domain.VncSession;
import com.tts.codelab.account.domain.VncSessionSummary;

public interface VncService {
    UserVncSession findBySessionId(String sessionId);

    VncSessionSummary findSummary();

    UserVncSession startNewVncSession(UserVncSession vncSession);

    UserVncSession stopBySessionId(String sessionId);
}
