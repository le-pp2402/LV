package com.phatpl.learnvocabulary.controllers;


import com.phatpl.learnvocabulary.dtos.request.SaveWordRequest;
import com.phatpl.learnvocabulary.dtos.response.WordResponse;
import com.phatpl.learnvocabulary.exceptions.UnauthorizationException;
import com.phatpl.learnvocabulary.filters.BaseFilter;
import com.phatpl.learnvocabulary.models.Word;
import com.phatpl.learnvocabulary.services.WordService;
import com.phatpl.learnvocabulary.utils.BuildResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/words")
public class WordController extends BaseController<Word, WordResponse, BaseFilter, Integer> {

    private final WordService wordService;

    public WordController(WordService wordService) {
        super(wordService);
        this.wordService = wordService;
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

//    @GetMapping("/w/")
//    public ResponseEntity findAll(@RequestBody Map<String, String> body) {
//        return BuildResponse.ok(wordService.findByDB(body.get("word")));
//    }

    @GetMapping("/w/")
    public ResponseEntity findWord(@RequestBody Map<String, String> body) {
        return BuildResponse.ok(wordService.findByTrie(body.get("word")));
    }
}
