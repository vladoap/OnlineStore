package com.example.MyStore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MyStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyStoreApplication.class, args);
    }

}
