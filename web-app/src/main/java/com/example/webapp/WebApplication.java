package com.example.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.auth.repository")
@EntityScan(basePackages = "com.example.auth.model")
public class WebApplication {
  public static void main(String[] args) {
    SpringApplication.run(WebApplication.class, args);
  }
}
