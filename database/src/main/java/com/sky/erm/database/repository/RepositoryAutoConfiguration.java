package com.sky.erm.database.repository;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.sky.erm.database.repository")
@EntityScan("com.sky.erm.database.record")
public class RepositoryAutoConfiguration {
}
