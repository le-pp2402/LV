package com.phatpl.learnvocabulary.controllers;

import com.phatpl.learnvocabulary.dtos.Response;
import com.phatpl.learnvocabulary.dtos.request.RegisterRequest;
import com.phatpl.learnvocabulary.dtos.request.VerifyEmailRequest;
import com.phatpl.learnvocabulary.services.UserService;
import com.phatpl.learnvocabulary.utils.BuildResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/register")
public class RegisterController {
    private final UserService userService;

    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            return BuildResponse.badRequest(errors.get(0).getDefaultMessage());
        } else {
            try {
                var obj = userService.register(request);
                return BuildResponse.created(obj);
            } catch (Exception e) {
                return BuildResponse.badRequest(e.getMessage());
            }
        }
    }

    @PutMapping("/verify")
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
