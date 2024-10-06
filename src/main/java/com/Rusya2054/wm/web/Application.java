package com.Rusya2054.wm.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.Rusya2054.wm.web.repositories")
@EntityScan(basePackages = "com.Rusya2054.wm.web.models")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
