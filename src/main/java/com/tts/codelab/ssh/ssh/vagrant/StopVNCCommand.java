package com.tts.codelab.ssh.ssh.vagrant;


import com.tts.codelab.ssh.ssh.domain.SimpleCommand;

public class StopVNCCommand extends SimpleCommand {

    public StopVNCCommand(int vncSession) {
        super("vncserver -kill :" + vncSession);
    }

}
