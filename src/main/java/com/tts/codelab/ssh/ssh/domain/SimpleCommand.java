package com.tts.codelab.ssh.ssh.domain;


import com.tts.codelab.ssh.domain.VagrantServer;
import com.tts.codelab.ssh.ssh.execute.SSHCommandExecutor;

public class SimpleCommand extends AbstractCommand implements UICommand{

    private String commandDescription;
    private String commandName;

    public SimpleCommand(String cmd) {
        this(cmd, null);
    }
    
    public SimpleCommand(String cmd, String sudoPass) {
        this(cmd, sudoPass, null, null);
    }
    
    public SimpleCommand(String cmd, String sudoPass, String commandName, String commandDescription) {
        super(cmd, sudoPass);
        this.commandName = commandName;
        this.commandDescription = commandDescription;
    }

    @Override
    public SSHResult execute(SSHCommandExecutor executor, String host, int port, String userName, String password) throws Exception {
        return executor.execute(host, port, userName, password, this);
    }

    @Override
    public String getName() {
        return this.commandName;
    }

    @Override
    public String getDescription() {
        return this.commandDescription;
    }

}
