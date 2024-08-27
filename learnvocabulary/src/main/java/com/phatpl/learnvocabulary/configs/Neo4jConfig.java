package com.phatpl.learnvocabulary.configs;

import org.neo4j.ogm.session.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@ComponentScan
@EnableNeo4jRepositories
@EnableTransactionManagement
public class Neo4jConfig {

    private static final Logger log = LoggerFactory.getLogger(Neo4jConfig.class);
    @Value("${NEO4J_URI}")
    private String URI;
    @Value("${NEO4J_USERNAME}")
    private String username;
    @Value("${NEO4J_PASSWORD}")
    private String password;
    @Value("${NEO4J_DB}")
    private String database;

    @Bean
    public SessionFactory sessionFactory() {
//        var config = new org.neo4j.ogm.config.Configuration.Builder()
//                .uri(URI)
//                .credentials(username, password)
//                .database(database)
//                .build();
        return new SessionFactory(configuration(), "com.phatpl.learnvocabulary.models.graph");
    }

    @Bean
    public org.neo4j.ogm.config.Configuration configuration() {
        return new org.neo4j.ogm.config.Configuration.Builder()
                .uri(URI)
                .credentials(username, password)
                .database(database)
                .build();
    }

    @Bean
    public Neo4jTransactionManager transactionManager() {
        return new Neo4jTransactionManager(sessionFactory());
    }


}
