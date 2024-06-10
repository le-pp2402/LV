package com.phatpl.learnvocabulary.controllers;

import com.phatpl.learnvocabulary.dtos.response.UserResponse;
import com.phatpl.learnvocabulary.filters.UserFilter;
import com.phatpl.learnvocabulary.models.User;
import com.phatpl.learnvocabulary.services.UserService;
import com.phatpl.learnvocabulary.utils.Logger;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/test")
    public ResponseEntity<?> findWithFilter(@RequestBody(required = false) UserFilter ft) {
        Logger.log(ft.getPageNumber().toString());
//        List<UserResponse> lst = userSev.findWithFilter(ft);
//        if (lst != null) {
//            return ResponseEntity.status(HttpStatus.OK).body(lst);
//        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NOT FOUND");
    }

}
