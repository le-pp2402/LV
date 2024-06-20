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

    public UserResponse getUserInfo() {
        return UserResponse.builder().elo(0).username("user").id(0).email("").isAdmin(false).build();
    }

    public Object login(LoginRequest userLogin) {
        var optionalUser = userRepository.findByUsername(userLogin.getUsername());
        if (optionalUser.isPresent() && BCryptPassword.matches(userLogin.getPassword(), optionalUser.get().getPassword())) {
            var user = optionalUser.get();
            if (!user.getActived()) return "active account first";
            return new LoginResponse(jwtService.genToken(userResponseMapper.toDTO(user)));
        }
        return "Wrong username or password";
    }

}
