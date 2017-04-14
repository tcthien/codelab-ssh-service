package com.tts.codelab.ssh.ssh.execute;


import com.tts.codelab.ssh.ssh.domain.Command;

public interface SSHCommandCallback {

    boolean preExecute(Command sshCommand);

    boolean postExecute(Command sshCommand);

}
