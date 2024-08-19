package com.sky.erm.service;

import com.sky.erm.auth.jwt.JwtUtil;
import com.sky.erm.dto.User;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * Service for authentication operations.
 */
@Service
@AllArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    public String getToken(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));
        String authEmail = authentication.getName();
        User user = User.builder().email(authEmail).build();
        return jwtUtil.createToken(user);
    }

}
