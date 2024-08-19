package com.sky.erm.controller;

import com.sky.erm.model.LoginRequest;
import com.sky.erm.model.LoginResponse;
import com.sky.erm.service.AuthenticationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/" + AuthenticationController.ENDPOINT)
@AllArgsConstructor
@Slf4j
public class AuthenticationController {

    public static final String ENDPOINT = "auth";

    private final AuthenticationService authenticationService;

    @ResponseBody
    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginReq)  {
        log.info("Login request: {}", loginReq.getEmail());
        String token = authenticationService.getToken(loginReq.getEmail(), loginReq.getPassword());
        LoginResponse loginResponse = new LoginResponse(loginReq.getEmail(), token);
        return ResponseEntity.ok(loginResponse);
    }

}
