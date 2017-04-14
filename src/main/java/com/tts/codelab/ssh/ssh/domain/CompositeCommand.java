package com.tts.codelab.ssh.ssh.domain;


import com.tts.codelab.ssh.domain.SSHServer;
import com.tts.codelab.ssh.ssh.execute.SSHCommandExecutor;

import java.util.ArrayList;
import java.util.List;

public abstract class CompositeCommand implements Command {
    
    protected List<Command> subCommands = new ArrayList<>();

    public List<Command> getSubCommands() {
        return subCommands;
    }
    
    @Override
    public String getTextCommand() {
        return null;
    }

    @Override
    public int getTimeout() {
        return -1;
    }
    
    @Override
    public SSHResult execute(SSHCommandExecutor executor, SSHServer server) throws Exception {
        return executor.execute(server.getServerIp(), server.getUserName(), server.getPassword(), this);
    }
}
