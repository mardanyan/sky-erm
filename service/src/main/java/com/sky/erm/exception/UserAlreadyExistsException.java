package com.sky.erm.exception;

/**
 * Exception thrown when a user already exists in the DB.
 */
public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String email, Exception e) {
        super("User already exists with email: " + email, e);
    }

}
