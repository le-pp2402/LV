package com.phatpl.learnvocabulary.controller;

import com.phatpl.learnvocabulary.model.User;
import com.phatpl.learnvocabulary.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userSev = new UserService();

//    @GetMapping("")
//    public ResponseEntity<User> findById(@PathVariable Integer id) {
//        User user = userSev.findById(id);
//        if (user != null) {
//        }
//    }
}
