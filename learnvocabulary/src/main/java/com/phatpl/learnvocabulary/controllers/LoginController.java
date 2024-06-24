package com.phatpl.learnvocabulary.controllers;


import com.phatpl.learnvocabulary.dtos.request.LoginRequest;
import com.phatpl.learnvocabulary.services.AuthService;
import com.phatpl.learnvocabulary.utils.BuildResponse;
import com.phatpl.learnvocabulary.utils.Logger;
import jakarta.persistence.PersistenceContextType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/login")
public class LoginController {
    private final AuthService authService;
    @Autowired
    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity login(@RequestBody LoginRequest loginRequest) {

        Logger.log("OK");
        try {
            log.debug(authService.login(loginRequest).toString()    );
            return BuildResponse.ok(authService.login(loginRequest));
        } catch (Exception e) {
            return BuildResponse.badRequest(e.getMessage());
        }
    }
}
