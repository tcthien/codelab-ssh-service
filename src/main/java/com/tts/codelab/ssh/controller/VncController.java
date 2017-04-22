package com.tts.codelab.ssh.controller;

import com.tts.codelab.ssh.domain.UserVncSession;
import com.tts.codelab.ssh.domain.VagrantBoxSession;
import com.tts.codelab.ssh.domain.VncSessionSummary;
import com.tts.codelab.ssh.service.VagrantBoxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/box")
public class BoxController {

    @Autowired
    private VagrantBoxService vagrantBoxService;

    @RequestMapping(path = "/", method = RequestMethod.POST)
    public UserVncSession provisionVagrantBox(Principal principal) throws Exception {
        String userName = principal.getName();
        return convertToUserVncSession(vagrantBoxService.provision(userName, "java-dev-box", 1024));

    }

    private UserVncSession convertToUserVncSession(VagrantBoxSession session) {
        // Convert to UserVncSession to suppress some important information
        if (session == null) {
            return UserVncSession.builder().build();
        }
        return UserVncSession.builder().sessionId(session.getSessionId()).userName(session.getOwner())
                .upTime(session.getProvisionTime()).build();
    }

    @RequestMapping(path = "/{sessionId}", method = RequestMethod.DELETE)
    public void destroyVagrantBox(Principal principal, @PathVariable String sessionId) throws Exception {
        vagrantBoxService.destroy(sessionId);
    }


    @RequestMapping(path = "/{sessionId}", method = RequestMethod.GET)
    public UserVncSession getVncSession(Principal principal, @PathVariable String sessionId) {
        return convertToUserVncSession(vagrantBoxService.getVagrantBoxSession(sessionId));
    }

//    @RequestMapping(path = "/", method = RequestMethod.GET)
//    public VncSessionSummary getVncSummary(Principal principal) {
//        return vncService.findSummary();
//    }

}
