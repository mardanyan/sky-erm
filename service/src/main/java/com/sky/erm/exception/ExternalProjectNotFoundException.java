package com.sky.erm.exception;

/**
 * Exception thrown when an external project is not found.
 */
public class ExternalProjectNotFoundException extends RuntimeException {

    public ExternalProjectNotFoundException(Long id) {
        super("External Project not found with id: " + id);
    }

}