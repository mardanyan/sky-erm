package com.sky.erm.service.controller;

import com.sky.erm.controller.AuthenticationController;
import com.sky.erm.database.repository.UserRepository;
import com.sky.erm.dto.User;
import com.sky.erm.model.LoginResponse;
import com.sky.erm.service.UserService;
import com.sky.erm.service.config.TestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = TestConfig.class
)
@ActiveProfiles("integration")
class AuthenticationControllerTest extends BaseAuthControllerTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    TestRestTemplate restTemplate;

    @Override
    String getEndPoint() {
        return AuthenticationController.ENDPOINT;
    }

    @Test
    void login() {
        ResponseEntity<LoginResponse> responseResponseEntity =  restTemplate.exchange(baseUrl + "/login",
                org.springframework.http.HttpMethod.POST,
                new org.springframework.http.HttpEntity<>(
                        User.builder().email(AUTH_USER_EMAIL).password(AUTH_USER_PASSWORD).build()),
                LoginResponse.class);

        assertThat(responseResponseEntity.getStatusCode()).isEqualTo(org.springframework.http.HttpStatus.OK);
        assertThat(responseResponseEntity.getBody())
                .extracting(LoginResponse::getToken)
                .isNotNull();
    }

    @DisplayName("Login with not existing user should fail")
    @Test
    void login_notExistingUser_fail() {
        ResponseEntity<?> responseResponseEntity =  restTemplate.exchange(baseUrl + "/login",
                org.springframework.http.HttpMethod.POST,
                new org.springframework.http.HttpEntity<>(
                        User.builder().email("not_existing_email").password("no_password").build()),
                String.class);

        assertThat(responseResponseEntity.getStatusCode().is4xxClientError()).isTrue();
    }

}
