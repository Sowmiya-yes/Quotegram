package com.myApp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableMongoAuditing
@EnableScheduling
//@EnableAutoConfiguration(exclude = {
//        DataSourceAutoConfiguration.class,
//        MongoAutoConfiguration.class,
//        MongoDataAutoConfiguration.class
//})
public class QuotegramApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuotegramApplication.class, args);
    }

}
