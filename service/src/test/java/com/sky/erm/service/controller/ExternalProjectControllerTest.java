package com.sky.erm.service.controller;

import com.sky.erm.controller.ExternalProjectController;
import com.sky.erm.database.repository.ExternalProjectRepository;
import com.sky.erm.database.repository.UserRepository;
import com.sky.erm.dto.ExternalProject;
import com.sky.erm.dto.User;
import com.sky.erm.service.config.TestConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
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
public class ExternalProjectControllerTest extends BaseAuthControllerTest {

    private static final String USER_EMAIL = "email";
    private static final String USER_PASSWORD = "password";

    @Autowired
    ExternalProjectRepository externalProjectRepository;
    @Autowired
    UserRepository userrepository;
    @Autowired
    TestRestTemplate restTemplate;

    User user;

    @Override
    String getEndPoint() {
        return ExternalProjectController.ENDPOINT;
    }

    @BeforeAll
    void init() {
        user = userService.addUser(User.builder().email(USER_EMAIL).password(USER_PASSWORD).build());
    }

    @BeforeEach
    void setUp() {
        externalProjectRepository.deleteAll();
    }

    @Test
    void addExternalProject() {
        ResponseEntity<ExternalProject> response = addExternalProjectAPI(user.getId(), "name", ExternalProject.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody())
                .isNotNull()
                .extracting(ExternalProject::getUserId, ExternalProject::getName)
                .containsExactly(user.getId(), "name");
    }

    @Test
    void deleteExternalProject() {
        ResponseEntity<ExternalProject> response = addExternalProjectAPI(user.getId(), "name", ExternalProject.class);
        assertThat(response.getStatusCode().is2xxSuccessful())
                .isTrue();
        ExternalProject externalProject = response.getBody();
        assertThat(externalProject).isNotNull();

        ResponseEntity<Boolean> deleteResponse = deleteExternalProjectAPI(externalProject.getId());

        assertThat(deleteResponse.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @DisplayName("Add multiple external projects for a user and get them all")
    @Test
    void getAllExternalProjects() {
        List.of("name1", "name2", "name3")
                .forEach(name -> addExternalProjectAPI(user.getId(), name, ExternalProject.class));

        ResponseEntity<List<ExternalProject>> response = getExternalProjectsAPI();

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody())
                .isNotNull()
                .hasSize(3)
                .extracting(ExternalProject::getName)
                .containsExactlyInAnyOrder("name1", "name2", "name3");
    }

    @Test
    void getExternalProjectsForUserId() {
        List.of("name1", "name2", "name3")
                .forEach(name -> addExternalProjectAPI(user.getId(), name, ExternalProject.class));

        ResponseEntity<List<ExternalProject>> response = getExternalProjectsForUserIdAPI(user.getId());

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody())
                .isNotNull()
                .hasSize(3)
                .extracting(ExternalProject::getName)
                .containsExactlyInAnyOrder("name1", "name2", "name3");

        assertThat(response.getBody())
                .isNotNull()
                .extracting(ExternalProject::getName)
                .containsExactlyInAnyOrder("name1", "name2", "name3");
    }

    private ResponseEntity<Boolean> deleteExternalProjectAPI(Long projectId) {
        return restTemplate.exchange(baseUrl + "/" + projectId,
                HttpMethod.DELETE,
                new org.springframework.http.HttpEntity<>(headers),
                Boolean.class);
    }

    private <T> ResponseEntity<T> addExternalProjectAPI(Long userId, String name, Class<T> response) {
        return restTemplate.exchange(baseUrl,
                org.springframework.http.HttpMethod.POST,
                new org.springframework.http.HttpEntity<>(
                        ExternalProject.builder().userId(userId).name(name).build(),
                        headers),
                response);
    }

    private ResponseEntity<List<ExternalProject>> getExternalProjectsAPI() {
        return restTemplate.exchange(baseUrl,
                org.springframework.http.HttpMethod.GET,
                new org.springframework.http.HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {}
        );
    }

    private ResponseEntity<List<ExternalProject>> getExternalProjectsForUserIdAPI(Long userId) {
        return restTemplate.exchange(baseUrl + "/user-id/" + userId,
                org.springframework.http.HttpMethod.GET,
                new org.springframework.http.HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {}
        );
    }

}
