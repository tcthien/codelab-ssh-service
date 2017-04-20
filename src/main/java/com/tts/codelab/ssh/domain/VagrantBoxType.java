package com.tts.codelab.ssh.domain;

/**
 * Created by tcthien on 4/20/2017.
 */
public enum VagrantBoxType {
    JAVA_DEV_BOX("java-dev-box");

    private String name;

    VagrantBoxType(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
