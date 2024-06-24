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
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseService<User, UserResponse, UserFilter, Integer> {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final MailService mailService;
    private final UserResponseMapper userResponseMapper;

    @Autowired
    public UserService(UserResponseMapper userResponseMapper, UserRepository userRepository, MailService mailService) {
        super(userResponseMapper, userRepository);
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.userResponseMapper = userResponseMapper;
    }

    public UserResponse register(RegisterRequest request) throws RuntimeException, InterruptedException {
        String username = request.getUsername();
        String email = request.getEmail();

        if (userRepository.findByEmail(email).isPresent()) {
            throw new ExistedException("email");
        } else if (userRepository.findByUsername(username).isPresent()) {
            throw new ExistedException("username");
        }

        User user = (User) RegisterRequestMapper.instance.toEntity(request);
        user.setPassword(BCryptPassword.encode(user.getPassword()));
        user.setCode(MailUtil.genCode());
        user.setActivated(false);

        userRepository.save(user);
        mailService.sendEmail(MailUtil.genMail(user.getEmail(), user.getCode()));

        return UserResponseMapper.instance.toDTO(user);
    }

    public UserResponse me(Authentication authetication) {
        User user = userRepository.findByUsername(authetication.getName()).orElseThrow(() -> new BadRequestException("user not found"));
        return userResponseMapper.toDTO(user);
    }

    public UserResponse activeUser(String userMail, Integer code) throws Exception {
        var optUser = userRepository.findByEmail(userMail);
        if (optUser.isPresent() && optUser.get().getCode().equals(code)) {
            var user = optUser.get();
            user.setActivated(true);
            return userResponseMapper.toDTO(userRepository.save(user));
        } else throw new WrongVerifyCode();
    }

    public UserResponse updateUserInfo(String oldPassword, String newPassword) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new EntityNotFoundException());
        log.info(user.getPassword());
        log.info(BCryptPassword.encode(oldPassword));
        if (BCryptPassword.matches(oldPassword, user.getPassword())) {
            user.setPassword(BCryptPassword.encode(newPassword));
            userRepository.save(user);
            return userResponseMapper.toDTO(user);
        } else {
            throw new WrongUsernameOrPassword();
        }
    }
}
