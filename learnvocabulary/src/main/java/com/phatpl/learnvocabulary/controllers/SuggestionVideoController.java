package com.phatpl.learnvocabulary.controllers;

import com.phatpl.learnvocabulary.dtos.request.CategorySearchRequest;
import com.phatpl.learnvocabulary.services.SuggestionVideoService;
import com.phatpl.learnvocabulary.utils.BuildResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/video/suggest")
public class SuggestionVideoController {
    private final SuggestionVideoService suggestionVideoService;

    @Autowired
    public SuggestionVideoController(SuggestionVideoService suggestionVideoService) {
        this.suggestionVideoService = suggestionVideoService;
    }

    @GetMapping
    public ResponseEntity<?> getSuggestionVideos(@RequestBody CategorySearchRequest request) {
        try {
            return BuildResponse.ok(suggestionVideoService.getSuggestionVideos(request.getCategoryIds()));
        } catch (Exception e) {
            return BuildResponse.badRequest(e.getMessage());
        }
    }
}
