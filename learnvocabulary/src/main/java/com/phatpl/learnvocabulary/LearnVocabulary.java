package com.phatpl.learnvocabulary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableNeo4jRepositories(
        basePackages = {"com.phatpl.learnvocabulary.repositories.graph", "com.phatpl.learnvocabulary.models.graph"}
)
@EnableJpaRepositories(
        basePackages = "com.phatpl.learnvocabulary.repositories.jpa"
)
public class LearnVocabulary {
    public static void main(String[] args) {
        SpringApplication.run(LearnVocabulary.class, args);
    }
}
