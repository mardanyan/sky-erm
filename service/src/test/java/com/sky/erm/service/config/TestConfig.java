package com.sky.erm.service.config;

import com.sky.erm.auth.jwt.JwtUtil;
import com.sky.erm.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration
public class TestConfig {

    @Autowired
    JwtUtil jwtUtil;

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> getPostgresContainer() {
        return new PostgreSQLContainer<>("postgres:16.4");
    }

    @Bean(name = "mytoken")
    public String token() {
        return jwtUtil.createToken(User.builder().email("email").password("password").build());
    }

}
