package com.sky.erm.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void updateNoPassword() {
        User user = new User();
        user.setPassword("password");
        user.updateNoPassword();
        assertThat(user.getPassword()).isEqualTo(User.NO_PASSWORD);
    }

}