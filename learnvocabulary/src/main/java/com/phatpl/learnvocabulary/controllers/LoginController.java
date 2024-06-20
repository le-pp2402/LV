package com.phatpl.learnvocabulary.controllers;


import com.phatpl.learnvocabulary.dtos.Response;
import com.phatpl.learnvocabulary.dtos.request.LoginRequest;
import com.phatpl.learnvocabulary.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {
    private final AuthService authService;
    @Autowired
    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity login(@RequestBody LoginRequest loginRequest) {
        Object response = authService.login(loginRequest);
        return Response.ok(response);
    }
}
