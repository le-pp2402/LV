package com.phatpl.learnvocabulary.controller;

import com.phatpl.learnvocabulary.dto.request.UserLogin;
import com.phatpl.learnvocabulary.mapper.UserMapper;
import com.phatpl.learnvocabulary.model.User;
import com.phatpl.learnvocabulary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService userSev;


    @Autowired
    public UserController(UserService userSev) {
        this.userSev = userSev;
    }


    @GetMapping("user/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        User user = this.userSev.findById(id);
        if (user != null) {
            return ResponseEntity.status(HttpStatus.OK).body(UserMapper.convertToUserInfo(user));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NOT FOUND");
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLogin userLogin) {
        User user = this.userSev.findByUsernameAndPassword(userLogin.getUsername(), userLogin.getPassword());
        if (user != null) {
            return ResponseEntity.status(HttpStatus.OK).body(UserMapper.convertToUserInfo(user));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Wrong username or password");
    }
}
