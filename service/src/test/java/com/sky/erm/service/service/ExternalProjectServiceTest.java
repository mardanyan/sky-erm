package com.sky.erm.service.service;

import com.sky.erm.database.repository.UserRepository;
import com.sky.erm.dto.ExternalProject;
import com.sky.erm.dto.User;
import com.sky.erm.service.ExternalProjectService;
import com.sky.erm.service.UserService;
import com.sky.erm.service.config.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = TestConfig.class
)
@ActiveProfiles("integration")
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ExternalProjectServiceTest {

    @Autowired
    PostgreSQLContainer<?> postgres;

    @Autowired
    ExternalProjectService externalProjectService;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    User user1;
    User user2;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        user1 = userService.addUser(User.builder().email("email").name("name").password("password").build());
        user2 = userService.addUser(User.builder().email("email2").name("name2").password("password2").build());
    }

    @Test
    void addExternalProject_savesExternalProjectAndReturnsSavedExternalProject() {
        ExternalProject externalProject = externalProjectService.addExternalProject(ExternalProject.builder().userId(user1.getId()).name("name").build());

        ExternalProject savedExternalProject = externalProjectService.getExternalProject(externalProject.getId());

        assertThat(savedExternalProject).isNotNull();
        assertThat(savedExternalProject.getId()).isEqualTo(externalProject.getId());
        assertThat(savedExternalProject.getName()).isEqualTo(externalProject.getName());
    }

    @Test
    void getExternalProjectsForUserId_returnsExternalProjectsForUserId() {
        List.of(ExternalProject.builder().userId(user1.getId()).name("project1").build(),
                ExternalProject.builder().userId(user1.getId()).name("project2").build())
                .forEach(externalProjectService::addExternalProject);

        assertThat(externalProjectService.getExternalProjectsForUserId(user1.getId()))
                .extracting(ExternalProject::getName)
                .contains("project1", "project2");
    }

    @Test
    void deleteExternalProjects_removesExternalProjects() {
        ExternalProject externalProject = externalProjectService.addExternalProject(ExternalProject.builder().userId(user1.getId()).name("project1").build());

        externalProjectService.deleteExternalProjects(externalProject.getId());

        assertThat(externalProjectService.getAllExternalProjects()).isEmpty();
    }

    @Test
    void getAllExternalProjects_returnsAllExternalProjects() {
        List.of(ExternalProject.builder().userId(user1.getId()).name("project1").build(),
                ExternalProject.builder().userId(user1.getId()).name("project2").build(),
                ExternalProject.builder().userId(user2.getId()).name("project3").build())
                .forEach(externalProjectService::addExternalProject);

        assertThat(externalProjectService.getAllExternalProjects())
                .extracting(ExternalProject::getName)
                .contains("project1", "project2", "project3");
    }

}
