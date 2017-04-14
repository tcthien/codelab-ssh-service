package com.tts.codelab.ssh.ssh.domain;

import com.tts.codelab.ssh.domain.SSHServer;
import com.tts.codelab.ssh.ssh.execute.SSHCommandExecutor;

public interface UICommand {
    String getName();

    String getDescription();

    SSHResult execute(SSHCommandExecutor executor, SSHServer server) throws Exception;
}
