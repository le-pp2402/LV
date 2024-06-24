package com.phatpl.learnvocabulary.controllers;


import com.phatpl.learnvocabulary.dtos.request.LoginRequest;
import com.phatpl.learnvocabulary.services.AuthService;
import com.phatpl.learnvocabulary.utils.BuildResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/login")
public class LoginController {
    private final AuthService authService;

    @Autowired
    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity login(@RequestBody LoginRequest loginRequest) {
        try {
            return BuildResponse.ok(authService.login(loginRequest));
        } catch (Exception e) {
            return BuildResponse.badRequest(e.getMessage());
        }
    }
}
