package com.sky.erm.service.controller;

import com.sky.erm.controller.AuthenticationController;
import com.sky.erm.database.repository.UserRepository;
import com.sky.erm.dto.User;
import com.sky.erm.model.LoginResponse;
import com.sky.erm.service.UserService;
import com.sky.erm.service.util.TokenUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Base class for testing controllers that require authentication and postgres integration.
 * // TODO split into separate classes
 */
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseAuthControllerTest {

    protected static final String AUTH_USER_EMAIL = "auth_email";
    protected static final String AUTH_USER_PASSWORD = "auth_password";

    protected final String baseUrl;
    private final String baseAuthUrl;

    @Value("${auth.initialize:false}")
    protected boolean authInitialize;

    protected HttpHeaders headers;

    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    UserService userService;
    @Autowired
    PostgreSQLContainer<?> postgres;
    @Autowired
    UserRepository userRepository;

    public BaseAuthControllerTest() {
        baseUrl = "/api/v1/" + getEndPoint();
        baseAuthUrl = "/api/v1/" + AuthenticationController.ENDPOINT;
    }

    @BeforeAll
    void setUpAll() {
        userRepository.deleteAll();
        userService.addUser(User.builder().email(AUTH_USER_EMAIL).password(AUTH_USER_PASSWORD).build());

        headers = authInitialize
                ? TokenUtil.getHeadersForJwt(getToken())
                : new HttpHeaders();
    }

    abstract String getEndPoint();

    protected String getToken() {
        ResponseEntity<LoginResponse> responseResponseEntity =  restTemplate.exchange(baseAuthUrl + "/login",
                org.springframework.http.HttpMethod.POST,
                new HttpEntity<>(
                        User.builder().email(AUTH_USER_EMAIL).password(AUTH_USER_PASSWORD).build()),
                LoginResponse.class);

        assertThat(responseResponseEntity.getStatusCode()).isEqualTo(org.springframework.http.HttpStatus.OK);
        assertThat(responseResponseEntity.getBody())
                .isNotNull()
                .extracting(LoginResponse::getToken)
                .isNotNull();

        return responseResponseEntity.getBody().getToken();
    }

}
