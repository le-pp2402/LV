package com.phatpl.learnvocabulary.controllers;

import com.phatpl.learnvocabulary.dtos.Response;
import com.phatpl.learnvocabulary.dtos.request.RegisterRequest;
import com.phatpl.learnvocabulary.dtos.request.VerifyEmailRequest;
import com.phatpl.learnvocabulary.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Response> register(@RequestBody @Valid RegisterRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            return ResponseEntity.ok(Response.builder().code(HttpStatus.NOT_ACCEPTABLE.value()).message(errors.get(0).getDefaultMessage()).data("ERROR").build());
        } else {
            try {
                return ResponseEntity.ok(Response.builder().code(HttpStatus.CREATED.value()).data(userService.register(request)).message("created").build());
            } catch (Exception e) {
                return ResponseEntity.ok(Response.builder().code(HttpStatus.CREATED.value()).data(e.getMessage()).message("created").build());
            }
        }
    }

    @PutMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody VerifyEmailRequest request) {
        return ResponseEntity.ok(userService.activeUser(request.getId(), request.getCode()));
    }

}
