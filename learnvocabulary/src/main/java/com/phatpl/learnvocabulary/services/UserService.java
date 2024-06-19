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
import com.phatpl.learnvocabulary.utils.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService extends BaseService<User, UserResponse, UserFilter, Integer> {
    private final UserRepository userRepository;
    private final MailService mailService;
    @Autowired
    public UserService(UserResponseMapper userResponseMapper, UserRepository userRepository, MailService mailService) {
        super(userResponseMapper, userRepository);
        this.userRepository = userRepository;
        this.mailService = mailService;
    }

    public UserResponse register(RegisterRequest request) throws RuntimeException {
        if (!userRepository.findOneByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email exists");
        }
        if (!userRepository.findOneByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username exists");
        }
        User user = RegisterRequestMapper.instance.toEntity(request);
        user.setPassword(BCryptPassword.encode(user.getPassword()));
        user.setCode(MailUtil.genCode());

        userRepository.save(user);

        mailService.sendEmail(MailUtil.genMail(user.getEmail(), user.getCode()));

        var userOpt = userRepository.findByUsername(user.getUsername());
        return UserResponseMapper.instance.toDTO(userOpt.get(0));
    }

    public Response me(String token) {
        try {
            if (JWTService.isValid(token, ))
            Map<String, Object> obj = (Map<String, Object>) body.get("data");
            User user = userRepository.findById((Integer)obj.get("id")).get();
            return Response.builder()
                    .code(HttpStatus.OK.value())
                    .data(UserResponseMapper.instance.toDTO(user))
                    .build();
        } catch (RuntimeException e) {
            return Response.builder()
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .data(e.getMessage())
                    .build();
        }
    }

    public Response activeUser(String userMail, Integer code) {
        var users = userRepository.findByEmail(userMail);
        if (users.isEmpty() || users.get(0).getCode().equals(code)) {
            return Response.builder().code(HttpStatus.NOT_FOUND.value()).message("Invalid code").build();
        }
        users.get(0).setActived(true);
        userRepository.save(users.get(0));
        return Response.builder().code(HttpStatus.OK.value()).message("Active successful").build();
    }

    public Response updateUserInfo(String token, String oldPassword, String newPassword) {
        try {
            JWTService.isValid(token);
            var body = JWTService.isValid(token).getBody();
            Map<String, Object> obj = (Map<String, Object>) body.get("data");
            var user = userRepository.findOneByUsername((String)obj.get("username")).get();
            if (BCryptPassword.matches(oldPassword, user.getPassword())) {
                user.setPassword(BCryptPassword.encode(newPassword));
                userRepository.save(user);
            } else {
                return Response.builder().code(HttpStatus.NOT_ACCEPTABLE.value()).data("").message("Wrong password").build();
            }
        } catch (RuntimeException e) {
            return Response.builder().code(HttpStatus.NOT_ACCEPTABLE.value()).data("").message(e.getMessage()).build();
        }
        return Response.builder().code(HttpStatus.OK.value()).data("").message("update successful").build();
    }
}
