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
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            return ResponseEntity.ok(Response.builder().code(HttpStatus.NOT_ACCEPTABLE.value()).message(errors.get(0).getDefaultMessage()).data("ERROR").build());
        } else {
            try {
                Object obj = userService.register(request);
                return Response.created(obj);
            } catch (Exception e) {
                var response = Response.builder().code(HttpStatus.CREATED.value()).data(e.getMessage()).message("created").build();
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
            }
        }
    }

    @PutMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody VerifyEmailRequest request) {
        return ResponseEntity.ok(
                Response.ok(
                    userService.activeUser(request.getMail(), request.getCode())
                )
        );
    }

}
