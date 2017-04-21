package com.tts.codelab.ssh.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Getter
@Setter
@Builder
/**
 * Every Server should be oganized as following:
 *     - vagrantRootFolder: Root Folder: /vagrant
 *     - vagrantSubFolder: Sub folder where vagrant session will be provisioned:
 *          + /vagrant/user1
 *          + /vagrant/user2
 */
public class VagrantServer {

    private String serverIp;

    private int port;

    private String userName;

    private String password;

    private String vagrantRootFolder;

    private Map<String, Integer> vagrantSubFolder;
}
