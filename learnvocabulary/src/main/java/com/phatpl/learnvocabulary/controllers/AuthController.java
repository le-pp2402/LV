package com.phatpl.learnvocabulary.controllers;

import com.phatpl.learnvocabulary.dtos.Response;
import com.phatpl.learnvocabulary.dtos.request.RegisterRequest;
import com.phatpl.learnvocabulary.services.UserService;
import com.phatpl.learnvocabulary.services.UserServiceTest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserServiceTest userService;

    @Autowired
    public AuthController(UserServiceTest userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    private ResponseEntity<Response> register(@RequestBody @Valid RegisterRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            return ResponseEntity.ok(Response.builder().code(HttpStatus.NOT_ACCEPTABLE.value()).message(errors.get(0).getDefaultMessage()).data(new String("ERROR")).build());
        } else {
            return ResponseEntity.ok(Response.builder().code(HttpStatus.CREATED.value()).data(userService.register(request)).message("created").build());
        }
    }

}
