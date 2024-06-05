package com.phatpl.learnvocabulary.controller;

import com.phatpl.learnvocabulary.dto.request.RegisterRequest;
import com.phatpl.learnvocabulary.service.UserService;
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
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    private ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request, BindingResult bindingResult) {
        ResponseEntity<?> response;
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            response = ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errors.get(0).getDefaultMessage());
        } else {
            response = ResponseEntity.status(HttpStatus.OK).body(userService.register(request));
        }
        return response;
    }

}
