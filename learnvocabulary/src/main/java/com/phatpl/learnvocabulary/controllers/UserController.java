package com.phatpl.learnvocabulary.controllers;

import com.phatpl.learnvocabulary.dtos.request.UpdatePasswordRequest;
import com.phatpl.learnvocabulary.dtos.response.UserResponse;
import com.phatpl.learnvocabulary.filters.UserFilter;
import com.phatpl.learnvocabulary.models.User;
import com.phatpl.learnvocabulary.services.JWTService;
import com.phatpl.learnvocabulary.services.UserService;
import com.phatpl.learnvocabulary.utils.BuildResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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

    @PutMapping("/me")
    public ResponseEntity updateUserInfo(HttpServletRequest request,
                                            HttpServletResponse response,
                                            @RequestBody @Valid UpdatePasswordRequest updatePasswordRequest,
                                            BindingResult bindingResult) {
        var token = request.getHeader("Authorization").substring(7);

        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            return BuildResponse.unauthorized(errors.get(0).getDefaultMessage());
        } else {
            try {
                String oldToken = request.getHeader("Authorization").substring(7);
                response.addCookie(new Cookie("token", jwtService.refreshToken(token)));
                return
                        BuildResponse.ok(
                                userService.updateUserInfo(
                                    token,
                                    updatePasswordRequest.getOldPassword(),
                                    updatePasswordRequest.getNewPassword()
                                )
                );
            } catch (Exception e) {
                return BuildResponse.badRequest(e.getMessage());
            }
        }
    }

    @GetMapping("/me")
    public ResponseEntity getUserInfo(HttpServletRequest request) {
        var token = request.getHeader("Authorization").substring(7);
        try {
            var response = userService.me(token);
            if (response == null) return BuildResponse.notFound(response);
            return BuildResponse.ok(response);
        } catch (Exception e) {
            return BuildResponse.badRequest(e.getMessage());
        }
    }

}
