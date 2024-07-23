package com.phatpl.learnvocabulary.services;

import com.phatpl.learnvocabulary.dtos.request.RegisterRequest;
import com.phatpl.learnvocabulary.dtos.response.UserResponse;
import com.phatpl.learnvocabulary.exceptions.BadRequestException;
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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public class UserService extends BaseService<User, UserResponse, UserFilter, Integer> {
    UserRepository userRepository;
    MailService mailService;
    UserResponseMapper userResponseMapper;

    @Autowired
    public UserService(UserResponseMapper userResponseMapper, UserRepository userRepository, MailService mailService) {
        super(userResponseMapper, userRepository);
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.userResponseMapper = userResponseMapper;
    }

    public UserResponse register(RegisterRequest request) throws RuntimeException {
        String username = request.getUsername();
        String email = request.getEmail();

        if (userRepository.findByEmail(email).isPresent()) {
            throw new ExistedException("email");
        } else if (userRepository.findByUsername(username).isPresent()) {
            throw new ExistedException("username");
        }

        User user = RegisterRequestMapper.instance.toEntity(request);
        user.setPassword(BCryptPassword.encode(user.getPassword()));
        user.setCode(MailUtil.genCode());
        user.setActivated(false);

        persistEntity(user);
        mailService.sendEmail(MailUtil.genMail(user.getEmail(), user.getCode()));

        return UserResponseMapper.instance.toDTO(user);
    }

    public UserResponse me(Authentication authetication) {
        User user = userRepository.findByUsername(authetication.getName()).orElseThrow(() -> new BadRequestException("user not found"));
        return userResponseMapper.toDTO(user);
    }

    public UserResponse activeUser(String userMail, Integer code) {
        var optUser = userRepository.findByEmail(userMail);
        if (optUser.isPresent() && optUser.get().getCode().equals(code)) {
            var user = optUser.get();
            user.setActivated(true);
            return userResponseMapper.toDTO(persistEntity(user));
        } else throw new WrongVerifyCode();
    }

    public UserResponse updateUserInfo(String oldPassword, String newPassword, JwtAuthenticationToken auth) {
        var user = findById(extractUserId(auth));
        if (BCryptPassword.matches(oldPassword, user.getPassword())) {
            user.setPassword(BCryptPassword.encode(newPassword));
            persistEntity(user);
            return userResponseMapper.toDTO(user);
        } else {
            throw new WrongUsernameOrPassword();
        }
    }
}
