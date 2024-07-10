package com.phatpl.learnvocabulary.utils;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
public class TextToJsonArray {
    public static String ReadFile(MultipartFile file) throws IOException {
        var input = file.getInputStream();
        byte[] line = input.readAllBytes();
        StringBuilder content = new StringBuilder();
        for (byte b : line) {
            if (Character.isLetter(b) || Character.isDigit(b)) {
                content.append(Character.toString(b));
            } else if (Character.isSpaceChar(b)) {
                content.append(" ");
            }
        }
        return content.toString();
    }

    public static JSONArray toJsonArray(String str) {
        String[] words = str.split(" ");
        JSONArray jsonArray = new JSONArray();
        for (String word : words)
            jsonArray.put(word);
        return jsonArray;
    }
}
