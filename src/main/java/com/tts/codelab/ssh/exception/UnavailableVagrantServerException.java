package com.tts.codelab.ssh.exception;

/**
 * Created by tcthien on 4/21/2017.
 */
public class UnavailableVagrantServerException extends RuntimeException {

    public UnavailableVagrantServerException(){
        super("There is no Vagrant server available at the moment.");
    }
}
