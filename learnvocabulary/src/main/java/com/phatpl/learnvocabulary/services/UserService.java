package com.phatpl.learnvocabulary.services;

import com.phatpl.learnvocabulary.dtos.request.RegisterRequest;
import com.phatpl.learnvocabulary.dtos.response.UserResponse;
import com.phatpl.learnvocabulary.exceptions.ExistedException;
import com.phatpl.learnvocabulary.exceptions.WrongUsernameOrPassword;
import com.phatpl.learnvocabulary.exceptions.WrongVerifyCode;
import com.phatpl.learnvocabulary.filters.UserFilter;
import com.phatpl.learnvocabulary.mappers.RegisterRequestMapper;
import com.phatpl.learnvocabulary.mappers.UserResponseMapper;
import com.phatpl.learnvocabulary.models.User;
import com.phatpl.learnvocabulary.repositories.UserRepository;
import com.phatpl.learnvocabulary.utils.BCryptPassword;
import com.phatpl.learnvocabulary.utils.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService extends BaseService<User, UserResponse, UserFilter, Integer> {
    private final UserRepository userRepository;
    private final MailService mailService;
    private final JWTService jwtService;
    @Autowired
    public UserService(UserResponseMapper userResponseMapper, UserRepository userRepository, MailService mailService, JWTService jwtService) {
        super(userResponseMapper, userRepository);
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.jwtService = jwtService;
    }

    public UserResponse register(RegisterRequest request) throws RuntimeException {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ExistedException("email");
        }
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new ExistedException("username");
        }
        User user = (User) RegisterRequestMapper.instance.toEntity(request);
        user.setPassword(BCryptPassword.encode(user.getPassword()));
        user.setCode(MailUtil.genCode());

        userRepository.save(user);

        mailService.sendEmail(MailUtil.genMail(user.getEmail(), user.getCode()));

        var userOpt = userRepository.findByUsername(user.getUsername());
        return (UserResponse) UserResponseMapper.instance.toDTO(userOpt.get());
    }

    public UserResponse me(String token) throws Exception {
        var body = jwtService.verifyToken(token).getBody();
        Map<String, Object> obj = (Map<String, Object>) body.get("data");
        User user = userRepository.findById((Integer)obj.get("id")).get();
        return UserResponseMapper.instance.toDTO(user);
    }

    public UserResponse activeUser(String userMail, Integer code) throws Exception {
        var optUser = userRepository.findByEmail(userMail);
        if (optUser.isPresent() && optUser.get().getCode().equals(code)) {
            var user = optUser.get();
            user.setActivated(true);
            return UserResponseMapper.instance.toDTO(userRepository.save(user));
        }
        throw new WrongVerifyCode();
    }

    public UserResponse updateUserInfo(String token, String oldPassword, String newPassword) throws RuntimeException {
        var body = jwtService.verifyToken(token).getBody();
        Map<String, Object> obj = (Map<String, Object>) body.get("data");
        var user = userRepository.findByUsername((String)obj.get("username")).get();
        if (BCryptPassword.matches(oldPassword, user.getPassword())) {
            user.setPassword(BCryptPassword.encode(newPassword));
        } else {
            throw new WrongUsernameOrPassword();
        }
        return UserResponseMapper.instance.toDTO(userRepository.save(user));
    }
}
