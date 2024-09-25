package com.sky.erm.service.service;

import com.sky.erm.database.repository.UserRepository;
import com.sky.erm.dto.User;
import com.sky.erm.service.UserService;
import com.sky.erm.service.config.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = TestConfig.class
)
@ActiveProfiles("integration")
@Testcontainers
class UserServiceTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.4");

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;
    private TransactionTemplate transactionTemplate;

    @SpyBean
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void addUser_savesUserAndReturnsSavedUser() {
        User user = userService.addUser(User.builder().email("email").name("name").password("password").build());

        User savedUser = userService.getUser(user.getId());

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isEqualTo(user.getId());
    }

    @Test
    void deleteUser_removesUserAndReturnsTrue() {
        User user = userService.addUser(User.builder().email("email").name("name").password("password").build());

        userService.deleteUser(user.getId());

        List<User> allUsers = userService.getUsers();
        assertThat(allUsers).isEmpty();
    }

    @Test
    void getUser() {
        User user = userService.addUser(User.builder().email("email").name("name").password("password").build());

        User foundUser = userService.getUser(user.getId());

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getId()).isEqualTo(user.getId());
    }

    @Test
    void getPassword_returnProtectedPassword() {
        User user = userService.addUser(User.builder().email("email").name("name").password("password").build());

        User foundUser = userService.getUser(user.getId());

        assertThat(foundUser).extracting(User::getPassword).isEqualTo(User.NO_PASSWORD);
    }

    @Test
    void updateUser() {
        User user = userService.addUser(User.builder().email("email").name("name").password("password").build());

        User updatedUser = userService.updateUser(user.getId(), User.builder().id(user.getId()).email("email2").name("name2").build());

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getId()).isEqualTo(user.getId());
        assertThat(updatedUser.getEmail()).isEqualTo("email2");
        assertThat(updatedUser.getName()).isEqualTo("name2");
    }

    @Test
    void updateAndDeleteUserParallel_userDeleted() {
        transactionTemplate = new TransactionTemplate(transactionManager);
        User user = userService.addUser(User.builder().email("email").name("name").password("password").build());

        CountDownLatch latchWait = new CountDownLatch(2);

        doAnswer(invocation -> {
            latchWait.countDown();
            awaitLatch(latchWait);
            return invocation.callRealMethod();
        }).when(passwordEncoder).encode(anyString());

        // update user in one transaction
        final CompletableFuture<Void> updateTx = CompletableFuture.runAsync(() -> transactionTemplate.execute(status -> {
            userService.updateUser(user.getId(), User.builder().id(user.getId()).name("name2").password("password2").build());
            return null;
        }));

        // delete user in another transaction
        final CompletableFuture<Void> deleteTx = CompletableFuture.runAsync(() -> {
            latchWait.countDown();
            awaitLatch(latchWait);
            userService.deleteUser(user.getId());
        });

        CompletableFuture.allOf(updateTx, deleteTx).join();

        assertThat(userService.getUsers()).isEmpty();
    }

    private static void awaitLatch(CountDownLatch latch) {
        try {
            latch.await(20, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

}
