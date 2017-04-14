package com.tts.codelab.ssh.ssh.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SSHResult {
    public static int STATUS_OK = 1;
    public static int STATUS_NOK = 0;

    public static int PING_UNREACHABLE = 0;
    public static int PING_REACHABLE = 1;

    private int exitStatus;
    private String text;
    private String outputText;

    public SSHResult() {

    }
}
