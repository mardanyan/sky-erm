package com.sky.erm.database.repository;

import com.sky.erm.database.record.ExternalProjectRecord;
import com.sky.erm.database.record.UserRecord;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest()
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestContainersConfiguration.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExternalProjectRepositoryTest  {

    @Autowired
    PostgreSQLContainer postgres;

    @Autowired
    ExternalProjectRepository externalProjectRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    void existingUser_findByUserId_returnsExternalProject() {
        UserRecord userRecord = createUserRecord();
        createExternalProjectRecord(userRecord.getId(), "project1");
        createExternalProjectRecord(userRecord.getId(), "project2");

        List<ExternalProjectRecord> foundExternalProjects = externalProjectRepository.findByUserId(userRecord.getId());
        assertThat(foundExternalProjects)
                .hasSize(2)
                .extracting("name")
                .contains("project1", "project2");
    }

    UserRecord createUserRecord() {
        UserRecord userRecord = UserRecord.builder()
                .email("email")
                .password("password")
                .name("name")
                .build();
        return userRepository.save(userRecord);
    }

    ExternalProjectRecord createExternalProjectRecord(Long userId, String name) {
        ExternalProjectRecord externalProjectRecord = ExternalProjectRecord.builder()
                .userId(userId)
                .name(name)
                .build();
        return externalProjectRepository.save(externalProjectRecord);
    }

}
