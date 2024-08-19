package com.sky.erm.database.repository;

import com.sky.erm.database.record.UserRecord;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
@DataJpaTest()
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestContainersConfiguration.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositoryTest {

    @Autowired
    PostgreSQLContainer postgres;

    @Autowired
    UserRepository userRepository;

    @Test
    void existingUser_findByEmail_returnsUser() {
        UserRecord userRecord = createRecord(1L, "email");
        userRepository.save(userRecord);

        assertThat(userRepository.findByEmail("email"))
                .isPresent()
                .hasValueSatisfying(user -> {
                    assertThat(user.getId()).isEqualTo(1L);
                    assertThat(user.getEmail()).isEqualTo("email");
                });
    }

    UserRecord createRecord(Long id, String email) {
        return UserRecord.builder()
                .id(id)
                .email(email)
                .password("password")
                .name("name")
                .build();
    }

}
