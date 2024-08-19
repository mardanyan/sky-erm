package com.sky.erm.config;

import com.sky.erm.database.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * UserDetailsService implementation that uses the UserRepository to authenticate users.
 */
@Component
@AllArgsConstructor
public class UserRepositoryUserDetailsService implements UserDetailsService {

    private final String[] ROLES_USER = {"USER"};

    private final UserRepository userRepository;

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(userRecord -> org.springframework.security.core.userdetails.User.builder()
                        .username(userRecord.getEmail())
                        .password(userRecord.getPassword())
                        .roles(ROLES_USER)
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
