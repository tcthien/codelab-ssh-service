package com.tts.codelab.account.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Document(collection = "vnc_sessions")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Getter
@Setter
@Builder
public class VncSession {

    @Id
    private String sessionId;

    @NotNull
    private String userName;

    @NotNull
    private String description;

    @NotNull
    private String serverIp;

    @NotNull
    private int vncSessionId;

    private Date upTime = new Date();
}
