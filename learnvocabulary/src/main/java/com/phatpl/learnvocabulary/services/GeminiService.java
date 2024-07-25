package com.phatpl.learnvocabulary.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phatpl.learnvocabulary.utils.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class GeminiService {
    private static final Logger log = LoggerFactory.getLogger(GeminiService.class);
    @Value("${GOOGLE_KEY}")
    private String apiKey;

    @Value("${GOOGLE_API}")
    private String link;

    private static final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

    private String createPromt(String query) {
        String sysInstruction = "Summarize the following passage: ";
        return Constant.PROMT_PREFIX + sysInstruction + query + Constant.PROMT_SUFFIX;
    }

    public String generator(String query) {
        try {
            System.out.println(createPromt(query));
            HttpRequest req = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(createPromt(query)))
                    .uri(URI.create(link + apiKey))
                    .header("Content-type", Constant.CT_APPLICATION_JSON)
                    .build();
            System.out.println(req.uri());
            HttpResponse<String> res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            System.out.println(res.body().toString());
            return extractInformation(res);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    private String extractInformation(HttpResponse<String> response) throws JsonProcessingException {
        var result = new ObjectMapper().readValue(response.body(), HashMap.class);
        var result1 = (ArrayList<HashMap<String, Object>>) result.get("candidates");
        var result2 = (HashMap<String, Object>) result1.get(0).get("content");
        var result3 = (ArrayList<HashMap<String, Object>>) result2.get("parts");
        return (String) result3.get(0).get("text");
    }

//    private static final String projectId = "gen-lang-client-0650801001";
//    private static final String modelName = "gemini-1.5-flash-001";
//    private static final String prefixPrompt = "Summarize the following passage: ";
//
//    public static String gen(String textPrompt) {
//        try {
//            VertexAI vertexAI = new VertexAI(projectId, null);
//            GenerativeModel model = new GenerativeModel(modelName, vertexAI);
//            GenerateContentResponse response = model.generateContent(prefixPrompt + textPrompt);
//            log.info("{} : {}", "kết quả: ", ResponseHandler.getText(response));
//
//            return ResponseHandler.getText(response);
//        } catch (Exception e) {
//            throw new RuntimeException(e.getMessage());
//        }
//    }
}
