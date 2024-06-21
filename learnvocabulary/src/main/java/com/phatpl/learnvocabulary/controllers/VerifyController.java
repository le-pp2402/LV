package com.phatpl.learnvocabulary.controllers;

import com.phatpl.learnvocabulary.dtos.Response;
import com.phatpl.learnvocabulary.dtos.request.RegisterRequest;
import com.phatpl.learnvocabulary.dtos.request.VerifyEmailRequest;
import com.phatpl.learnvocabulary.services.UserService;
import com.phatpl.learnvocabulary.utils.BuildResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/verify")
public class VerifyController {

    private final UserService userService;

    @Autowired
    public VerifyController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping
    public ResponseEntity<?> verify(@RequestBody VerifyEmailRequest request) {
        try {
            return BuildResponse.ok(
                    userService.activeUser(request.getMail(), request.getCode())
            );
        } catch (Exception e) {
            return BuildResponse.badRequest(e.getMessage());
        }
    }

}
