package com.phatpl.learnvocabulary.controllers;

import com.phatpl.learnvocabulary.dtos.response.UserResponse;
import com.phatpl.learnvocabulary.services.UserServiceTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserServiceTest userSev;

    @Autowired
    public UserController(UserServiceTest userSev) {
        this.userSev = userSev;
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        UserResponse userResponse = userSev.findById(id);
        if (userResponse != null) {
            return ResponseEntity.status(HttpStatus.OK).body(userResponse);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NOT FOUND");
    }


    @GetMapping
    public ResponseEntity<?> findAll() {
        List<UserResponse> users = this.userSev.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }
}
