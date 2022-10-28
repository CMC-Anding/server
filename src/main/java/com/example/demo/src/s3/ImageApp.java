package com.example.demo.src.s3;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ImageApp {

    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.yml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(com.example.demo.src.s3.ImageApp.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }
}
