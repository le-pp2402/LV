package com.phatpl.learnvocabulary;


import com.phatpl.learnvocabulary.utils.BuildTrie;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class LearnVocabulary {
    public static BuildTrie buildTrie = new BuildTrie();

    public static void main(String[] args) {
        SpringApplication.run(LearnVocabulary.class, args);

    }

}
