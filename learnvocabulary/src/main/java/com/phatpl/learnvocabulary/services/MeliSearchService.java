package com.phatpl.learnvocabulary.services;

import com.phatpl.learnvocabulary.utils.TextToJsonArray;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Slf4j
@Service
public class MeliSearchService {

    private final com.meilisearch.sdk.Index index;

    @Autowired
    public MeliSearchService(com.meilisearch.sdk.Index index) {
        this.index = index;
    }


    public void addDocument(Integer uid, String title, MultipartFile document) {
        try {
            String data = TextToJsonArray.ReadFile(document);
            var content = TextToJsonArray.toJsonArray(data);
            JSONArray array = new JSONArray();
            ArrayList<JSONObject> items = new ArrayList<>() {{
                add(new JSONObject().put("id", uid).put("title", title).put("content", content));
            }};
            array.put(items);
            String documents = array.getJSONArray(0).toString();
            index.addDocuments(documents);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }

    public void removeDocument() {

    }
}

