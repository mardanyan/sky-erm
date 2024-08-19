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

    protected Long id;

    protected String email;

    protected String password;

    protected String name;

}
