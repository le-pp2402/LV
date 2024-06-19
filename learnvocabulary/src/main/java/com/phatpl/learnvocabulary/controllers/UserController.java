package com.phatpl.learnvocabulary.controllers;

import com.phatpl.learnvocabulary.dtos.Response;
import com.phatpl.learnvocabulary.dtos.request.UpdatePasswordRequest;
import com.phatpl.learnvocabulary.dtos.response.UserResponse;
import com.phatpl.learnvocabulary.filters.UserFilter;
import com.phatpl.learnvocabulary.models.User;
import com.phatpl.learnvocabulary.services.JWTService;
import com.phatpl.learnvocabulary.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController extends BaseController<User, UserResponse, UserFilter, Integer> {

    private final UserService userService;
    private final JWTService jwtService;
    public UserController(UserService userService, JWTService jwtService) {
        super(userService);
        this.userService = userService;
        this.jwtService = jwtService;
    }

//    @PutMapping("/me")
//    public ResponseEntity<?> updateUserInfo(HttpServletRequest request,
//                                            @RequestBody @Valid UpdatePasswordRequest updatePasswordRequest,
//                                            BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            List<FieldError> errors = bindingResult.getFieldErrors();
//            return ResponseEntity.ok(Response.builder().code(HttpStatus.NOT_ACCEPTABLE.value()).message(errors.get(0).getDefaultMessage()).data("invalid password").build());
//        } else {
//            try {
//                String oldToken = request.getHeader("Authorization").substring(7);
//                return ResponseEntity.ok(userService.updateUserInfo(request.getHeader("Authorization").substring(7),
//                        updatePasswordRequest.getOldPassword(),
//                        updatePasswordRequest.getNewPassword()));
//            } catch (Exception e) {
//                return ResponseEntity.ok(e.getMessage());
//            }
//        }
//    }

    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo() {
        return ResponseEntity.ok(userService.me());
    }

}
