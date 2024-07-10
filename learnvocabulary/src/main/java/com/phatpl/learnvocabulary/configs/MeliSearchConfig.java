package com.phatpl.learnvocabulary.configs;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MeliSearchConfig {
    @Value("${MeliSearch_PORT}")
    private String url;

    @Value("${MeliSearch_MASTER_KEY}")
    private String masterKey;

    @Bean
    public Config config() {
        return new Config(url, masterKey);
    }

    @Bean
    public Client client() {
        return new Client(config());
    }

    @Bean
    public com.meilisearch.sdk.Index index() {
        return client().index("document");
    }
}
