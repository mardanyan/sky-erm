package com.sky.erm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a user.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class User {

    public static final String NO_PASSWORD = "********";

    protected Long id;

    protected String email;

    protected String password;

    protected String name;

    /**
     * Set password to {@link #NO_PASSWORD} to avoid exposing real password.
     */
    public void setNoPassword() {
        password = NO_PASSWORD;
    }

}
