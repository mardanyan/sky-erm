package com.sky.erm.dto;

import lombok.AllArgsConstructor;

/**
 * Decorator for {@link User} to hide password.
 */
@AllArgsConstructor
public class PasswordProtectedUser extends User {

    public static String PASSWORD = "********";

    private final User user;

    @Override
    public Long getId() {
        return user.getId();
    }

    @Override
    public String getEmail() {
        return user.getEmail();
    }

    @Override
    public String getPassword() {
        return PASSWORD;
    }

    @Override
    public String getName() {
        return user.getName();
    }

}
