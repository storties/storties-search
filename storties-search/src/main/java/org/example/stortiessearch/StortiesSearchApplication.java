package org.example.stortiessearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StortiesSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(StortiesSearchApplication.class, args);
    }
}
