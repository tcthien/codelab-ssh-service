package com.tts.codelab.ssh.ssh.vagrant;


import com.tts.codelab.ssh.ssh.domain.SimpleCommand;

public class VagrantDestroyCommand extends SimpleCommand {

    /**
     * @param vagrantSubFolder place where vagrant box will be provisioned
     */
    public VagrantDestroyCommand(String vagrantSubFolder) {
        super("cd " + vagrantSubFolder + " && vagrant destroy && rm -rf " + vagrantSubFolder + "/.lock ");
    }

}
