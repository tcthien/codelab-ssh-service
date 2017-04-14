package com.tts.codelab.account.service;

import com.tts.codelab.account.domain.UserVncSession;
import com.tts.codelab.account.domain.VncSessionSummary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VncServiceImpl implements VncService {
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
        return null;
    }

    @Override
    public UserVncSession stopBySessionId(String sessionId) {
        return null;
    }
}
