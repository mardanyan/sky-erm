package com.sky.erm.config;

import com.sky.erm.exception.ExternalProjectNotFoundException;
import com.sky.erm.exception.UserAlreadyExistsException;
import com.sky.erm.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {

    @Value("${exception.handler.message.enabled:true}")
    private boolean exceptionOverrideEnabled;

    @Value("${exception.handler.message}")
    private String message;

    @ExceptionHandler({UserNotFoundException.class, ExternalProjectNotFoundException.class})
    public ResponseEntity<String> handle(UserNotFoundException e) {
        if (exceptionOverrideEnabled) {
            return getGenericExceptionMessage();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler({UserAlreadyExistsException.class, BadCredentialsException.class})
    public ResponseEntity<String> handledUserException(Exception e) {
        if (exceptionOverrideEnabled) {
            return getGenericExceptionMessage();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<String> handle(Exception e) {
        if (exceptionOverrideEnabled) {
            return getGenericExceptionMessage();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    private ResponseEntity<String> getGenericExceptionMessage() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

}
