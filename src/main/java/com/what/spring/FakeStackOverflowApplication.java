package com.what.spring;

import org.springframework.web.filter.CorsFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableTransactionManagement
@SpringBootApplication
@EnableConfigurationProperties
public class FakeStackOverflowApplication {

    public static void main(String[] args) {
        SpringApplication.run(FakeStackOverflowApplication.class, args);
    }

}
