package com.sky.erm.config;

import com.sky.erm.exception.ExternalProjectNotFoundException;
import com.sky.erm.exception.UserAlreadyExistsException;
import com.sky.erm.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
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
    private String genericMessage;

    @ExceptionHandler({UserNotFoundException.class, ExternalProjectNotFoundException.class})
    public ResponseEntity<String> handleNotFound(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getMessage(e.getMessage()));
    }

    @ExceptionHandler({UserAlreadyExistsException.class, BadCredentialsException.class})
    public ResponseEntity<String> handledBadRequest(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getMessage(e.getMessage()));
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<String> handleConflict(Exception e) {
        String message = getRootCause(e).getMessage();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(getMessage(message));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<String> handle(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(getMessage(e.getMessage()));
    }

    private String getMessage(String message) {
        return exceptionOverrideEnabled ? genericMessage : message;
    }

    private static Throwable getRootCause(Throwable t) {
        Throwable rootCause = NestedExceptionUtils.getRootCause(t);
        return rootCause != null ? rootCause : t;
    }

}
