package com.sky.erm.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void setNoPassword() {
        User user = new User();
        user.setPassword("password");
        user.setNoPassword();
        assertThat(user.getPassword()).isEqualTo(User.NO_PASSWORD);
    }

}