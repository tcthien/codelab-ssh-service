package com.tts.codelab.ssh.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import javax.validation.constraints.NotNull;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Getter
@Setter
@Builder
public class UserVncSession {

    private String sessionId;

    @NotNull
    private String userName;

    @NotNull
    private String description;

    private Date upTime = new Date();
}
