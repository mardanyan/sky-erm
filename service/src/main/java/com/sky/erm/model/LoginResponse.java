package com.sky.erm.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response object for login requests.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginResponse {

    private String email;

    private String token;

}
