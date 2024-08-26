package com.sky.erm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class SkyErmApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkyErmApplication.class, args);
    }

}
