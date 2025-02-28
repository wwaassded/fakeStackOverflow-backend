package com.what.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
@EnableConfigurationProperties
public class FakeStackOverflowApplication {

    public static void main(String[] args) {
        SpringApplication.run(FakeStackOverflowApplication.class, args);
    }

}
