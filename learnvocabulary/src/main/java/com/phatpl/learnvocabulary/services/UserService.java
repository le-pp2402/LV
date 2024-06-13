package com.phatpl.learnvocabulary.services;

import com.phatpl.learnvocabulary.dtos.Response;
import com.phatpl.learnvocabulary.dtos.request.RegisterRequest;
import com.phatpl.learnvocabulary.dtos.response.UserResponse;
import com.phatpl.learnvocabulary.filters.UserFilter;
import com.phatpl.learnvocabulary.mappers.RegisterRequestMapper;
import com.phatpl.learnvocabulary.mappers.UserResponseMapper;
import com.phatpl.learnvocabulary.models.User;
import com.phatpl.learnvocabulary.repositories.UserRepository;
import com.phatpl.learnvocabulary.utils.BCryptPassword;
import com.phatpl.learnvocabulary.utils.Logger;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService extends BaseService<User, UserResponse, UserFilter, Integer> {
    private final UserRepository userRepository;
    @Autowired
    public UserService(UserResponseMapper userResponseMapper, UserRepository userRepository) {
        super(userResponseMapper, userRepository);
        this.userRepository = userRepository;
    }

    public UserResponse register(RegisterRequest request) throws Exception {
        if (userRepository.findByEmail(request.getEmail()).size() != 0) {
            throw new RuntimeException("Email exists");
        }
        if (userRepository.findByUsername(request.getUsername()).size() != 0) {
            throw new RuntimeException("Username exists");
        }
        User user = RegisterRequestMapper.instance.toEntity(request);
        user.setPassword(BCryptPassword.encode(user.getPassword()));
        userRepository.save(user);
        var userOpt = userRepository.findByUsername(user.getUsername());
        return UserResponseMapper.instance.toDTO(userOpt.get(0));
    }

    public Response me(String token) {
        UserResponse userResponse;
        try {
            var body = JWTService.verifyToken(token).getBody();
            Map<String, Object> obj = (Map<String, Object>) body.get("data");
            userResponse = UserResponse.builder()
                    .id((Integer) obj.get("id"))
                    .username((String) obj.get("username"))
                    .email((String) obj.get("email"))
                    .isAdmin((Boolean) obj.get("is_admin"))
                    .elo((Integer) obj.get("elo"))
                    .build();
        } catch (ExpiredJwtException e) {
            Logger.log(e.getMessage());
            return Response.builder().code(HttpStatus.NOT_ACCEPTABLE.value()).data("").message("Login session expired").build();
        } catch (Exception e) {
            Logger.log(e.getMessage());
            return Response.builder().code(HttpStatus.NOT_ACCEPTABLE.value()).data("").message("Invalid login session").build();
        }
        return Response.builder().code(HttpStatus.OK.value()).data(userResponse).message("Success").build();
    }
}
