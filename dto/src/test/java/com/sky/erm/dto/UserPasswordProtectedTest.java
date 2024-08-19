package com.sky.erm.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserPasswordProtectedTest {

    @Test
    void user_wrappedInUserPasswordProtected_returnsReplacedFixedPassword() {
        User user = User.builder()
                .password("real-password")
                .build();

        PasswordProtectedUser userPasswordProtected = new PasswordProtectedUser(user);
        assertThat(userPasswordProtected.getPassword()).isEqualTo(PasswordProtectedUser.PASSWORD);
    }

}
