package com.tts.codelab.ssh.ssh.vagrant;


import com.tts.codelab.ssh.ssh.domain.SimpleCommand;

public class StartVNCCommand extends SimpleCommand {

    public StartVNCCommand(int vncSession) {
        super("vncserver :" + vncSession);
    }

}
