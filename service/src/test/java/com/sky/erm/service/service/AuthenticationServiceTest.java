package com.sky.erm.service.service;

import com.sky.erm.database.repository.UserRepository;
import com.sky.erm.dto.User;
import com.sky.erm.service.AuthenticationService;
import com.sky.erm.service.UserService;
import com.sky.erm.service.config.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = TestConfig.class
)
@ActiveProfiles("integration")
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthenticationServiceTest {

    private static final String USER_EMAIL = "email";
    private static final String USER_PASSWORD = "password";

    @Autowired
    PostgreSQLContainer<?> postgres;

    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserService userService;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        userService.addUser(User.builder().email(USER_EMAIL).password(USER_PASSWORD).build());
    }

    @Test
    void getToken() {
        String token = authenticationService.getToken(USER_EMAIL, USER_PASSWORD);

        assertThat(token).isNotBlank();
    }

}
