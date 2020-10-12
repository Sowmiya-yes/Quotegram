package com.myApp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QuotegramApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(QuotegramApplication.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("Welcome to Quotegram Application");
    }
}
