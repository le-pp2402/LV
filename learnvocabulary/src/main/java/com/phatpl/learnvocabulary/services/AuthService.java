package com.phatpl.learnvocabulary.services;

import com.phatpl.learnvocabulary.dtos.Response;
import com.phatpl.learnvocabulary.dtos.request.LoginRequest;
import com.phatpl.learnvocabulary.dtos.response.LoginResponse;
import com.phatpl.learnvocabulary.dtos.response.UserResponse;
import com.phatpl.learnvocabulary.filters.UserFilter;
import com.phatpl.learnvocabulary.mappers.UserResponseMapper;
import com.phatpl.learnvocabulary.models.User;
import com.phatpl.learnvocabulary.repositories.UserRepository;
import com.phatpl.learnvocabulary.utils.BCryptPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService extends BaseService<User, UserResponse, UserFilter, Integer> {
    private final UserRepository userRepository;
    private final UserResponseMapper userResponseMapper;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @Autowired
    public AuthService(UserResponseMapper userResponseMapper, UserRepository userRepository, AuthenticationManager authenticationManager, JWTService jwtService) {
        super(userResponseMapper, userRepository);
        this.userRepository = userRepository;
        this.userResponseMapper = userResponseMapper;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public static UserResponse getUserInfo() {
        return UserResponse.builder().elo(0).username("user").id(0).email("").isAdmin(false).build();
    }

    public Response login(LoginRequest userLogin) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLogin.getUsername(),
                        BCryptPassword.encode(userLogin.getPassword())
                )
        );
        var user = userRepository.findByUsername(userLogin.getUsername()).orElseThrow(()-> new UsernameNotFoundException("wrong username"));
        var userResponse = userResponseMapper.toDTO(user);
        return Response.builder().data(
                new LoginResponse(jwtService.genDefaultUserToken(userResponse, (UserDetails) user))
        ).build();
    }
}
