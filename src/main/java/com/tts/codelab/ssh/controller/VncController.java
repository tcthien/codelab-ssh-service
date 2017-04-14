package com.tts.codelab.ssh.controller;

import com.tts.codelab.ssh.domain.UserVncSession;
import com.tts.codelab.ssh.domain.VncSessionSummary;
import com.tts.codelab.ssh.service.VncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/vnc")
public class VncController {

    @Autowired
    private VncService vncService;

    @RequestMapping(path = "/{sessionId}", method = RequestMethod.GET)
    public UserVncSession getVncSession(Principal principal, @PathVariable String sessionId) {
        return vncService.findBySessionId(sessionId);
    }

    @RequestMapping(path = "/{sessionId}", method = RequestMethod.DELETE)
    public UserVncSession deleteVncSession(Principal principal, @PathVariable String sessionId) {
        return vncService.stopBySessionId(sessionId);
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public VncSessionSummary getVncSummary(Principal principal) {
        return vncService.findSummary();
    }

    @RequestMapping(path = "/", method = RequestMethod.POST)
    public UserVncSession startNewVncSession(@Valid @RequestBody UserVncSession vncSession) {
        return vncService.startNewVncSession(vncSession);
    }
}
