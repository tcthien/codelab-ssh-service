package com.tts.codelab.ssh.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class ErrorHandler {

    // TODO add MethodArgumentNotValidException handler
    // TODO remove such general handler
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void processValidationError(IllegalArgumentException e) {
        log.error("Returning HTTP 400 Bad Request", e);
    }
}
