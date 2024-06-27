package com.phatpl.learnvocabulary.controllers;

import com.phatpl.learnvocabulary.dtos.request.SaveWordRequest;
import com.phatpl.learnvocabulary.dtos.response.WordResponse;
import com.phatpl.learnvocabulary.exceptions.UnauthorizationException;
import com.phatpl.learnvocabulary.filters.BaseFilter;
import com.phatpl.learnvocabulary.models.Word;
import com.phatpl.learnvocabulary.services.WordHintService;
import com.phatpl.learnvocabulary.services.WordService;
import com.phatpl.learnvocabulary.utils.BuildResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/words")
public class WordController extends BaseController<Word, WordResponse, BaseFilter, Integer> {

    private final WordService wordService;
    private final WordHintService wordHintService;

    public WordController(WordService wordService, WordHintService wordHintService) {
        super(wordService);
        this.wordService = wordService;
        this.wordHintService = wordHintService;
    }

    @PostMapping("/{id}")
    public ResponseEntity saveIntoGroup(@RequestBody SaveWordRequest request) {
        try {
            JwtAuthenticationToken auth = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            return BuildResponse.ok(
                    wordService.saveIntoGroup(request, auth)
            );
        } catch (UnauthorizationException e) {
            return BuildResponse.unauthorized(e.getMessage());
        } catch (RuntimeException e) {
            return BuildResponse.badRequest(e.getMessage());
        }
    }

    @PostMapping("/w")
    public ResponseEntity findWord(@RequestBody Map<String, String> prefix) {
//        return BuildResponse.ok(wordHintService.findByJPruningRadixTrie(prefix.get("word")));
        return BuildResponse.ok(wordHintService.findByTrie(prefix.get("word")));
    }
}
