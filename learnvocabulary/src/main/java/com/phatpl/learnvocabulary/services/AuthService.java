package com.phatpl.learnvocabulary.services;

import com.phatpl.learnvocabulary.dtos.request.LoginRequest;
import com.phatpl.learnvocabulary.dtos.response.LoginResponse;
import com.phatpl.learnvocabulary.dtos.response.UserResponse;
import com.phatpl.learnvocabulary.exceptions.BadRequestException;
import com.phatpl.learnvocabulary.exceptions.InactiveAccountException;
import com.phatpl.learnvocabulary.exceptions.WrongUsernameOrPassword;
import com.phatpl.learnvocabulary.filters.UserFilter;
import com.phatpl.learnvocabulary.mappers.LoginResponseMapper;
import com.phatpl.learnvocabulary.mappers.UserResponseMapper;
import com.phatpl.learnvocabulary.models.User;
import com.phatpl.learnvocabulary.repositories.UserRepository;
import com.phatpl.learnvocabulary.utils.BCryptPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService extends BaseService<User, UserResponse, UserFilter, Integer> {
    private final UserRepository userRepository;
    private final UserResponseMapper userResponseMapper;
    private final JWTService jwtService;
    @Autowired
    public AuthService(UserResponseMapper userResponseMapper, UserRepository userRepository, JWTService jwtService) {
        super(userResponseMapper, userRepository);
        this.userRepository = userRepository;
        this.userResponseMapper = userResponseMapper;
        this.jwtService = jwtService;
    }


    public LoginResponse login(LoginRequest userLogin) throws Exception {
        var optionalUser = userRepository.findByUsername(userLogin.getUsername());
        if (optionalUser.isPresent() && BCryptPassword.matches(userLogin.getPassword(), optionalUser.get().getPassword())) {
            var user = optionalUser.get();
            if (!user.getActivated())
                throw new InactiveAccountException();
            var loginResponse = LoginResponseMapper.instance.toDTO(user);
            loginResponse.setToken(jwtService.genToken(userResponseMapper.toDTO(user)));
            return loginResponse;
        } else {
            throw new WrongUsernameOrPassword();
        }
    }

}
