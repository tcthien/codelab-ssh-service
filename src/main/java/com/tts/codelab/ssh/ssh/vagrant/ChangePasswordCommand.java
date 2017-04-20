package com.tts.codelab.ssh.ssh.vagrant;


import com.tts.codelab.ssh.ssh.domain.SimpleCommand;

public class ChangePasswordCommand extends SimpleCommand {

    public ChangePasswordCommand(String user, String newPassword) {
        super("echo \"" + user + ":" + newPassword + "\" | sudo chpasswd");
    }

}
