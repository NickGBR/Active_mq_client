package com.example.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@EnableJms
@SpringBootApplication
public class clientApplication {

    public static void main(String[] args) {
        SpringApplication.run(clientApplication.class, args);
    }

}
