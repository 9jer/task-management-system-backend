package com.stasevich.taskmanagementsystembackend;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TaskManagementSystemBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskManagementSystemBackendApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
