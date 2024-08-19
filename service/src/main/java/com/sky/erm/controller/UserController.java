package com.sky.erm.controller;

import com.sky.erm.dto.User;
import com.sky.erm.dto.PasswordProtectedUser;
import com.sky.erm.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/" + UserController.ENDPOINT)
@AllArgsConstructor
@Slf4j
public class UserController {

    public static final String ENDPOINT = "user";

    private final UserService userService;

    @Operation(summary = "Add a new user")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@RequestBody User user) {
        log.info("Adding user: {}", user);
        return Optional.of(userService.addUser(user)).map(PasswordProtectedUser::new).get();
    }

    @Operation(summary = "Delete a user")
    @DeleteMapping("{id}")
    public boolean deleteUser(@PathVariable Long id) {
        log.info("Deleting user with id: {}", id);
        return userService.deleteUser(id);
    }

    @Operation(summary = "Get a user")
    @GetMapping("{id}")
    public User getUser(@PathVariable Long id) {
        log.info("Getting user with id: {}", id);
        return Optional.of(userService.getUser(id)).map(PasswordProtectedUser::new).get();
    }

    @Operation(summary = "Patch a user")
    @PatchMapping("{id}")
    public User update(@RequestBody User user, @PathVariable("id") Long id) {
        log.info("Updating user with id: {}", id);
        return userService.updateUser(id, user);
    }

    @Operation(summary = "Get all users")
    @GetMapping
    public List<? extends User> getAllUsers() {
        log.info("Getting all users");
        return userService.getUsers();
    }

}
