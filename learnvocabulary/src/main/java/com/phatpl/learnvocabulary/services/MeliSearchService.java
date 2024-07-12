package com.phatpl.learnvocabulary.services;

import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.model.Searchable;
import com.phatpl.learnvocabulary.utils.TextToJsonArray;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;

@Slf4j
@Service
public class MeliSearchService {

    private final com.meilisearch.sdk.Index index;

    @Autowired
    public MeliSearchService(com.meilisearch.sdk.Index index) {
        this.index = index;
    }

    public void addDocument(Integer uid, String title, InputStream document, Timestamp created, Boolean isPrivate) {
        try {
            String data = TextToJsonArray.ReadFile(document);
            var content = TextToJsonArray.toJsonArray(data);
            JSONArray array = new JSONArray();
            ArrayList<JSONObject> items = new ArrayList<>() {{
                add(
                        new JSONObject()
                                .put("id", uid)
                                .put("title", title)
                                .put("content", content)
                                .put("created", created)
                                .put("private", isPrivate)
                );
            }};

            array.put(items);
            String documents = array.getJSONArray(0).toString();
            index.addDocuments(documents);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }

    public Searchable search(SearchRequest request) {
        return index.search(request);
    }

    public void deleteById(Integer id) {
        index.deleteDocument(String.valueOf(id));
    }

    public void deleteAll() {
        index.deleteAllDocuments();
    }

    public void update(Integer id, String title, Boolean isPrivate) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("title", title);
        jsonObject.put("isPrivate", title);
        index.updateDocuments(jsonObject.toString());
    }
}

