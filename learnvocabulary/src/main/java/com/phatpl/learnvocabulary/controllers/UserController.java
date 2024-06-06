package com.phatpl.learnvocabulary.controllers;

import com.phatpl.learnvocabulary.dtos.response.UserResponse;
import com.phatpl.learnvocabulary.mappers.UserResponseMapper;
import com.phatpl.learnvocabulary.models.User;
import com.phatpl.learnvocabulary.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userSev;

    @Autowired
    public UserController(UserService userSev) {
        this.userSev = userSev;
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        User user = userSev.findById(id);
        if (user != null) {
            return ResponseEntity.status(HttpStatus.OK).body(UserResponseMapper.instance.toDTO(user));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NOT FOUND");
    }


    @GetMapping
    public ResponseEntity<?> findAll() {
        List<UserResponse> users = this.userSev.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }
}
