package com.example.urlcompact;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class UrlCompactApplication {

    public static void main(String[] args) {
        SpringApplication.run(UrlCompactApplication.class, args);
    }

}
