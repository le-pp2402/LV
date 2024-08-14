package com.phatpl.learnvocabulary.configs;

import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.AuthToken;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@Slf4j
public class Neo4jConfig {
    @Value("${NEO4J_URI}")
    private String URI;
    @Value("${NEO4J_USERNAME}")
    private String username;
    @Value("${NEO4J_PASSWORD}")
    private String password;

    @Bean
    public Driver driver() {
        try {
            System.err.println(username);
            System.err.println(password);
            System.err.println(URI);
            AuthToken auth = AuthTokens.basic(username, password);
            Driver driver = GraphDatabase.driver(URI, auth);
            driver.verifyConnectivity();
            log.info("Connect established");
            return driver;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
