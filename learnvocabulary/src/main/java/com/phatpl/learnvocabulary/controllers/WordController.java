package com.phatpl.learnvocabulary.controllers;

import com.phatpl.learnvocabulary.dtos.request.*;
import com.phatpl.learnvocabulary.dtos.response.WordResponse;
import com.phatpl.learnvocabulary.exceptions.UnauthorizationException;
import com.phatpl.learnvocabulary.filters.BaseFilter;
import com.phatpl.learnvocabulary.models.Word;
import com.phatpl.learnvocabulary.services.GroupWordService;
import com.phatpl.learnvocabulary.services.UserWordService;
import com.phatpl.learnvocabulary.services.WordHintService;
import com.phatpl.learnvocabulary.services.WordService;
import com.phatpl.learnvocabulary.utils.BuildResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/words")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WordController extends BaseController<Word, WordResponse, BaseFilter, Integer> {

    GroupWordService groupWordService;
    WordHintService wordHintService;
    UserWordService userWordService;
    WordService wordService;

    @Autowired
    public WordController(GroupWordService groupWordService, WordHintService wordHintService, UserWordService userWordService, WordService wordService) {
        super(wordService);
        this.groupWordService = groupWordService;
        this.wordHintService = wordHintService;
        this.userWordService = userWordService;
        this.wordService = wordService;
    }

    @Override
    @GetMapping
    @PreAuthorize("hasAuthority(SCOPE_ADMIN)")
    public ResponseEntity findAll(BaseFilter baseFilter) {
        List<WordResponse> lst = wordService.findAllDTO();
        return BuildResponse.ok(lst);
    }

    @PostMapping("/{id}")
    public ResponseEntity saveIntoGroup(@RequestBody SaveWordRequest request, @PathVariable("id") Integer wordId) {
        try {

            return BuildResponse.ok(
                    groupWordService.saveIntoGroup(request, wordId)
            );
        } catch (UnauthorizationException e) {
            return BuildResponse.unauthorized(e.getMessage());
        } catch (EntityNotFoundException e) {
            return BuildResponse.badRequest("Entity not found");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteFromGroup(@RequestBody DeleteWordRequest request, @PathVariable("id") Integer wordId) {
        try {

            return BuildResponse.ok(groupWordService.deleteFromGroup(request, wordId));
        } catch (UnauthorizationException e) {
            return BuildResponse.unauthorized(e.getMessage());
        } catch (RuntimeException e) {
            return BuildResponse.badRequest(e.getMessage());
        }
    }

    @PostMapping("/w")
    public ResponseEntity findWord(@RequestBody Map<String, String> prefix) {
        return BuildResponse.ok(wordHintService.findByTrie(prefix.get("word")));
    }

    @GetMapping("/me")
    public ResponseEntity getWordsOfUser() {
        try {

            return BuildResponse.ok(userWordService.getWordsOfUser());
        } catch (UnauthorizationException e) {
            return BuildResponse.unauthorized(e.getMessage());
        } catch (RuntimeException e) {
            return BuildResponse.badRequest(e.getMessage());
        }
    }

    @GetMapping("/me/{id}")
    public ResponseEntity getWordOfUser(@PathVariable("id") Integer wordId) {
        try {
            return BuildResponse.ok(userWordService.getWordOfUser(wordId));
        } catch (UnauthorizationException e) {
            return BuildResponse.unauthorized(e.getMessage());
        } catch (EntityNotFoundException e) {
            return BuildResponse.notFound("Ban chua luu tu nay");
        } catch (RuntimeException e) {
            return BuildResponse.badRequest(e.getMessage());
        }
    }

    @PostMapping("/me")
    public ResponseEntity saveWordsOfUser(@RequestBody WordsSaveRequest request) {
        try {
            return BuildResponse.ok(userWordService.saveWords(request));
        } catch (UnauthorizationException e) {
            return BuildResponse.unauthorized(e.getMessage());
        } catch (RuntimeException e) {
            return BuildResponse.badRequest(e.getMessage());
        }
    }

    @DeleteMapping("/me")
    public ResponseEntity deleteWordsOfUser(@RequestBody WordsDeleteRequest request) {
        try {
            return BuildResponse.ok(userWordService.deleteWords(request));
        } catch (UnauthorizationException e) {
            return BuildResponse.unauthorized(e.getMessage());
        } catch (RuntimeException e) {
            return BuildResponse.badRequest(e.getMessage());
        }
    }

    @PutMapping("/me")
    public ResponseEntity answer(@RequestBody ResultRequest request) {
        try {

            return BuildResponse.ok(userWordService.answer(request));
        } catch (UnauthorizationException e) {
            return BuildResponse.unauthorized(e.getMessage());
        } catch (RuntimeException e) {
            return BuildResponse.badRequest(e.getMessage());
        }
    }
}