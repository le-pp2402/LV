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
//        String masterKey = "EGm4C8zY64MU_VkeIL6SXlGAWtd7HDbNud-C1zQbNtA";
//        String port = "http://0.0.0.0:7700";
//        JSONArray array = new JSONArray();
//        ArrayList items = new ArrayList() {{
//            add(new JSONObject().put("id", "1").put("title", "Carol").put("genres", new JSONArray("[\"Romance\",\"Drama\"]")));
//            add(new JSONObject().put("id", "2").put("title", "Wonder Woman").put("genres", new JSONArray("[\"Action\",\"Adventure\"]")));
//            add(new JSONObject().put("id", "3").put("title", "Life of Pi").put("genres", new JSONArray("[\"Adventure\",\"Drama\"]")));
//            add(new JSONObject().put("id", "4").put("title", "Mad Max: Fury Road").put("genres", new JSONArray("[\"Adventure\",\"Science Fiction\"]")));
//            add(new JSONObject().put("id", "5").put("title", "Moana").put("genres", new JSONArray("[\"Fantasy\",\"Action\"]")));
//            add(new JSONObject().put("id", "6").put("title", "Philadelphia").put("genres", new JSONArray("[\"Drama\"]")));
//        }};
//
//        array.put(items);
//        String documents = array.getJSONArray(0).toString();
//        log.info(port);
//        log.info(masterKey);
//        Client client = new Client(new Config("http://localhost:55000/", masterKey));
//
//        // An index is where the documents are stored.
//        Index index = client.index("movies");
//
////         If the index 'movies' does not exist, Meilisearch creates it when you first add the documents.
//        index.addDocuments(documents); // => { "taskUid": 0 }
//        SearchResult results = index.search("a");
//        System.out.println(results);
    }

}
