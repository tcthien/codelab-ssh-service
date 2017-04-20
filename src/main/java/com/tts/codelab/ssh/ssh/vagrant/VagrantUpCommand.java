package com.tts.codelab.ssh.ssh.vagrant;


import com.tts.codelab.ssh.ssh.domain.SimpleCommand;

public class VagrantUpCommand extends SimpleCommand {

    /**
     * @param vagrantSubFolder place where vagrant box will be provisioned
     */
    public VagrantUpCommand(String vagrantSubFolder, String sessionId) {
        super("cd " + vagrantSubFolder +
                " && echo \"" + sessionId + "\" > " + vagrantSubFolder + "/.lock" +
                " && vagrant up");
    }

}
