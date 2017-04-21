package com.tts.codelab.ssh.ssh.execute;


import com.tts.codelab.ssh.ssh.domain.Command;
import com.tts.codelab.ssh.ssh.domain.SSHResult;

public interface SSHCommandExecutor {
    SSHResult execute(String host, String userName, String password, Command cmds) throws Exception;

    SSHResult execute(String host, String userName, String password, Command cmd, SSHCommandCallback callback) throws Exception;

    SSHResult execute(String host, int port, String userName, String password, Command cmd) throws Exception;

    SSHResult execute(String host, int port, String userName, String password, Command cmd, SSHCommandCallback callback) throws Exception;

    String getLog(String ipAddress);
}
