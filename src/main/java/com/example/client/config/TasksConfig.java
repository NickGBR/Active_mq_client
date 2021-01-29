package com.example.client.config;

import com.example.client.console.Console;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Configuration
public class TasksConfig {

    Console console;

    @Autowired
    public TasksConfig(Console console) {
        this.console = console;
    }

    @Bean("MyExecutor")
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }


    @Bean
    public CommandLineRunner schedulingRunner(@Qualifier("MyExecutor") TaskExecutor executor) {
        return args -> executor.execute(console);
    }
}

