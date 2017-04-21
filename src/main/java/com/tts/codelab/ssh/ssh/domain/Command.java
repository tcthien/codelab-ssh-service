package com.tts.codelab.ssh.ssh.domain;


import com.tts.codelab.ssh.domain.VagrantServer;
import com.tts.codelab.ssh.ssh.execute.SSHCommandExecutor;

public interface Command {
    String getTextCommand();
    
    int getTimeout();
    
    /**
     * Execute command on specific server
     * @return
     */
    SSHResult execute(SSHCommandExecutor executor, String host, int port, String userName, String password) throws Exception;
}
