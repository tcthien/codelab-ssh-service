package com.tts.codelab.ssh.ssh.domain;

import com.tts.codelab.ssh.domain.VagrantServer;
import com.tts.codelab.ssh.ssh.execute.SSHCommandExecutor;

public interface UICommand {
    String getName();

    String getDescription();

    SSHResult execute(SSHCommandExecutor executor, String host, int port, String userName, String password) throws Exception;
}
