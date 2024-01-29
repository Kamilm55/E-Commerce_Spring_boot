package com.example.kamil.main;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.example.kamil","com.example.kamil.user"})
public class MainApplication  {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

}
