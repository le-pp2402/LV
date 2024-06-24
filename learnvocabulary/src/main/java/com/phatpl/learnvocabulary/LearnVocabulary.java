package com.phatpl.learnvocabulary;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class LearnVocabulary {
    public static void main(String[] args) {
        SpringApplication.run(LearnVocabulary.class, args);
    }

}
