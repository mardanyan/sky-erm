package com.sky.erm.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Request object for login requests.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LoginRequest {

    @Email
    private String email;

    @NotEmpty
    private String password;

}
