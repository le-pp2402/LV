package com.phatpl.learnvocabulary.controller;

import com.phatpl.learnvocabulary.model.User;
import com.phatpl.learnvocabulary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userSev;


    @Autowired
    public UserController(UserService userSev) {
        this.userSev = userSev;
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        User user = this.userSev.findById(id);
        if (user != null) {
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(user);
    }

}
