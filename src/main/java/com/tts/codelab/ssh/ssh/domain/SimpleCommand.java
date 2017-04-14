package com.tts.codelab.ssh.ssh.domain;


import com.tts.codelab.ssh.domain.SSHServer;
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
    public SSHResult execute(SSHCommandExecutor executor, SSHServer server) throws Exception {
        return executor.execute(server.getServerIp(), server.getUserName(), server.getPassword(), this);
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
