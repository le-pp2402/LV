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
import com.phatpl.learnvocabulary.utils.MailUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;

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

    public Integer ranInt() {
        Random random = new Random();
        return 1000 + random.nextInt() % 1000;
    }
    public SimpleMailMessage genMail(String userEmail, int code) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("lephiphatphat@gmail.com");
        email.setText("Your code = " + String.valueOf(code));
        email.setTo(userEmail);
        return email;
    }

    public UserResponse register(RegisterRequest request) throws Exception {
        if (!userRepository.findByEmail(request.getEmail()).isEmpty()) {
            throw new RuntimeException("Email exists");
        }
        if (!userRepository.findByUsername(request.getUsername()).isEmpty()) {
            throw new RuntimeException("Username exists");
        }
        User user = RegisterRequestMapper.instance.toEntity(request);
        user.setPassword(BCryptPassword.encode(user.getPassword()));
        user.setCode(MailUtil.genCode());

        userRepository.save(user);

//        mailService.sendEmail(genMail(user.getEmail(), user.getCode()));

        mailService.sendEmail(MailUtil.genMail(user.getEmail(), user.getCode()));

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

    public Response activeUser(Integer userId, Integer code) {
        var user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return Response.builder().code(HttpStatus.NOT_FOUND.value()).message("Error").build();
        }
        String message = "active fail";
        Logger.log(code.toString());
        Logger.log(user.get().getCode().toString());
        if (user.get().getCode().equals(code)) {
            try {
                user.get().setActived(true);
                userRepository.save(user.get());
                message = "active successful";
                Logger.log("ERROR");
            } catch (Exception e) {
                Logger.error(e.getMessage());
            }
        }
        return Response.builder().code(HttpStatus.OK.value()).message(message).build();
    }
}
