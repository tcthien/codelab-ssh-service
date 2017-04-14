package com.tts.codelab.ssh.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Getter
@Setter
@Builder
public class VncSessionSummary {

    private int numberOfRunningSession;

    private int numberOfInactiveSession;

    private int numberOfAvailableSession;
}
