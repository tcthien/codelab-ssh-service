package com.tts.codelab.ssh.ssh.domain;


import com.tts.codelab.ssh.domain.SSHServer;
import com.tts.codelab.ssh.ssh.execute.SSHCommandExecutor;

public interface Command {
    String getTextCommand();
    
    int getTimeout();
    
    /**
     * Execute command on specific server
     * @param executor
     * @param server
     * @return
     */
    SSHResult execute(SSHCommandExecutor executor, SSHServer server) throws Exception;
}
