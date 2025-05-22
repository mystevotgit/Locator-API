package org.example.locatorapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class LocatorApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(LocatorApiApplication.class, args);
    }

}
