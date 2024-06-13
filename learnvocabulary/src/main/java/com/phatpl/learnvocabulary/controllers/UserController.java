package com.phatpl.learnvocabulary.controllers;

import com.phatpl.learnvocabulary.dtos.Response;
import com.phatpl.learnvocabulary.dtos.request.AuthRequest;
import com.phatpl.learnvocabulary.dtos.response.UserResponse;
import com.phatpl.learnvocabulary.filters.UserFilter;
import com.phatpl.learnvocabulary.models.User;
import com.phatpl.learnvocabulary.services.UserService;
import com.phatpl.learnvocabulary.utils.Logger;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController extends BaseController<User, UserResponse, UserFilter, Integer> {

    private final UserService userSev;

    public UserController(UserService userSev) {
        super(userSev);
        this.userSev = userSev;
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@NotNull @RequestBody AuthRequest request) {
        Response response = userSev.me(request.getToken());
        return ResponseEntity.ok(response);
    }
}
