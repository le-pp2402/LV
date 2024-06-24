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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService extends BaseService<User, UserResponse, UserFilter, Integer> {
    private final UserRepository userRepository;
    private final UserResponseMapper userResponseMapper;
    private final LoginResponseMapper loginResponseMapper;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(UserResponseMapper userResponseMapper, UserRepository userRepository, LoginResponseMapper loginResponseMapper, JWTService jwtService, AuthenticationManager authenticationManager) {
        super(userResponseMapper, userRepository);
        this.userRepository = userRepository;
        this.userResponseMapper = userResponseMapper;
        this.loginResponseMapper = loginResponseMapper;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }


    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new InactiveAccountException());
        if (!user.getActivated()) throw new InactiveAccountException();

        String token = jwtService.createToken(userResponseMapper.toDTO(user));
        var response = loginResponseMapper.toDTO(user);
        response.setToken(token);

        return response;
    }
}
