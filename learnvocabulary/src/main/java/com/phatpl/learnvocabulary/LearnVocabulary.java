package com.phatpl.learnvocabulary;

import com.phatpl.learnvocabulary.utils.Trie.BuildTrie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@Slf4j
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class LearnVocabulary {
    public static BuildTrie buildTrie = new BuildTrie();


    public static void main(String[] args) {
        SpringApplication.run(LearnVocabulary.class, args);
    }

}
