package com.phatpl.learnvocabulary.controller;

import com.phatpl.learnvocabulary.dto.response.UserResponse;
import com.phatpl.learnvocabulary.mapper.UserMapper;
import com.phatpl.learnvocabulary.model.User;
import com.phatpl.learnvocabulary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userSev;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userSev, UserMapper userMapper) {
        this.userSev = userSev;
        this.userMapper = userMapper;
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        User user = userSev.findById(id);
        if (user != null) {
            return ResponseEntity.status(HttpStatus.OK).body(UserMapper.instance.userToUserResponse(user));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NOT FOUND");
    }


    @GetMapping
    public ResponseEntity<?> findAll() {
        List<UserResponse> users = this.userSev.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }
}
