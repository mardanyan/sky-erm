package com.sky.erm.model;

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

    private String email;

    private String password;

}
