package com.tts.codelab.ssh.ssh.vagrant;


import com.tts.codelab.ssh.ssh.domain.SimpleCommand;

public class ChangeVNCPasswordCommand extends SimpleCommand {

    public ChangeVNCPasswordCommand(String newPassword) {
        super("echo " + newPassword + " | vncpasswd -f > ~/.vnc/passwd");
    }

}
