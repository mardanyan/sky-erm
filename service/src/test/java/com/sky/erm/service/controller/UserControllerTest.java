package com.sky.erm.service.controller;

import com.sky.erm.controller.UserController;
import com.sky.erm.database.repository.UserRepository;
import com.sky.erm.dto.User;
import com.sky.erm.model.LoginResponse;
import com.sky.erm.service.AuthenticationService;
import com.sky.erm.service.UserService;
import com.sky.erm.service.config.TestConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = TestConfig.class,
        properties = {
                "auth.initialize=true"
        }
)
@ActiveProfiles("integration")
@Slf4j
public class UserControllerTest extends BaseAuthControllerTest {

    static final String USER_EMAIL = "email";
    static final String USER_PASSWORD = "password";

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    private UserController userController;

    @Override
    String getEndPoint() {
        return UserController.ENDPOINT;
    }

    @BeforeEach
    void setUp() {
        userRepository.findByEmail(USER_EMAIL).ifPresent(userRepository::delete);
    }

    @Test
    void addUser() {
        ResponseEntity<LoginResponse> response = addUserAPI(USER_EMAIL, USER_PASSWORD, LoginResponse.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    void addUserTwice_fails() {
        ResponseEntity<?> response = addUserAPI(USER_EMAIL, USER_PASSWORD, LoginResponse.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        response = addUserAPI(USER_EMAIL, USER_PASSWORD, String.class);

        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    void deleteUser() {
        ResponseEntity<User> response = addUserAPI(USER_EMAIL, USER_PASSWORD, User.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody())
                .isNotNull()
                .extracting(User::getId)
                .isNotNull();
        Long userId = response.getBody().getId();

        ResponseEntity<Boolean> deleteResponse = deleteUserAPI(userId);

        assertThat(deleteResponse.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    void getUser() {
        ResponseEntity<User> response = addUserAPI(USER_EMAIL, USER_PASSWORD, User.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody())
                .isNotNull()
                .extracting(User::getId)
                .isNotNull();

        Long userId = response.getBody().getId();

        ResponseEntity<User> getResponse =  restTemplate.exchange(baseUrl + "/" + userId,
                org.springframework.http.HttpMethod.GET,
                new org.springframework.http.HttpEntity<>(headers),
                User.class);

        assertThat(getResponse.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(getResponse.getBody())
                .isNotNull()
                .returns(userId, User::getId)
                .returns(USER_EMAIL, User::getEmail)
                .returns(User.NO_PASSWORD, User::getPassword);
    }

    @Test
    void updateUser() {
        ResponseEntity<User> response = addUserAPI(USER_EMAIL, USER_PASSWORD, User.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody())
                .isNotNull()
                .returns(null, User::getName)
                .extracting(User::getId)
                .isNotNull();

        Long userId = response.getBody().getId();

        ResponseEntity<User> updateResponse = patchUserAPI(userId, User.builder().name("name").build());

        assertThat(updateResponse.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(updateResponse.getBody())
                .isNotNull()
                .returns(userId, User::getId)
                .returns(USER_EMAIL, User::getEmail)
                .returns("name", User::getName);
    }

    @Test
    void getAllUsers() {
        List.of(Pair.of("email1", "password1"),
                Pair.of("email2", "password2"),
                Pair.of("email3", "password3"))
                .forEach(pair -> addUserAPI(pair.getLeft(), pair.getRight(), User.class));

        ResponseEntity<List<User>> response = getUsersAPI();

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        List<User> users = response.getBody();

        assertThat(users)
                .isNotNull()
                .hasSize(4);

        assertThat(users)
                .extracting(User::getEmail)
                .contains("email1", "email2", "email3", AUTH_USER_EMAIL);
    }

    private <T> ResponseEntity<T> addUserAPI(String email, String password, Class<T> response) {
        return restTemplate.exchange(baseUrl,
                org.springframework.http.HttpMethod.POST,
                new org.springframework.http.HttpEntity<>(
                        User.builder().email(email).password(password).build(),
                        headers),
                response);
    }

    private ResponseEntity<List<User>> getUsersAPI() {
        return restTemplate.exchange(baseUrl,
                org.springframework.http.HttpMethod.GET,
                new org.springframework.http.HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {}
        );
    }

    private ResponseEntity<User> patchUserAPI(Long userId, User user) {
        return restTemplate.exchange(baseUrl + "/" + userId,
                org.springframework.http.HttpMethod.PATCH,
                new org.springframework.http.HttpEntity<>(
                        user,
                        headers),
                User.class);
    }

    private ResponseEntity<Boolean> deleteUserAPI(Long userId) {
        return restTemplate.exchange(baseUrl + "/" + userId,
                org.springframework.http.HttpMethod.DELETE,
                new org.springframework.http.HttpEntity<>(headers),
                Boolean.class);
    }

}
