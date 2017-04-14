package com.tts.codelab.ssh.ssh.execute;

public interface SSHConsoleLog {

    void log(String ipAddress, String outputConsole);
    
    String getLog(String ipAddress);

}
