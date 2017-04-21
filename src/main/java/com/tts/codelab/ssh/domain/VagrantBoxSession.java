package com.tts.codelab.ssh.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Document(collection = "vagrant_box_session")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Getter
@Setter
@Builder
public class VagrantBoxSession {

    @Id
    private String sessionId;

    @NotNull
    private String boxName;

    @NotNull
    @Builder.Default
    private VagrantBoxType boxType = VagrantBoxType.JAVA_DEV_BOX;

    @Builder.Default
    private int memory = 1024;

    @Builder.Default
    private int sshPort = 22;

    @Builder.Default
    private int vncPort = 5901;

    @NotNull
    private String owner; // Who is owner of this vagrant box session

    @Builder.Default
    private Date provisionDate = new Date();

    @NotNull
    private String serverIp;

    @NotNull
    private String vagrantSubFolder;
}
