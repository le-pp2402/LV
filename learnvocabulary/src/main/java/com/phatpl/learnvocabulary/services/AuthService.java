package com.phatpl.learnvocabulary.services;

import com.phatpl.learnvocabulary.dtos.Response;
import com.phatpl.learnvocabulary.dtos.request.LoginRequest;
import com.phatpl.learnvocabulary.dtos.response.LoginResponse;
import com.phatpl.learnvocabulary.dtos.response.UserResponse;
import com.phatpl.learnvocabulary.filters.UserFilter;
import com.phatpl.learnvocabulary.mappers.BaseMapper;
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

    @Autowired
    public AuthService(UserResponseMapper userResponseMapper, UserRepository userRepository) {
        super(userResponseMapper, userRepository);
        this.userRepository = userRepository;
        this.userResponseMapper = userResponseMapper;
    }

    public static UserResponse getUserInfo() {
        return UserResponse.builder().elo(0).username("user").id(0).email("").isAdmin(false).build();
    }

    public Response login(LoginRequest userLogin) {
        Boolean loginSuccess = true;
        User user = null;
        if (userRepository.findByUsername(userLogin.getUsername()).isEmpty()) {
            loginSuccess = false;
        } else {
            user = userRepository.findByUsername(userLogin.getUsername()).get(0);
            if (!BCryptPassword.matches(userLogin.getPassword(), user.getPassword())) {
                loginSuccess = false;
            }
        }
        if (!loginSuccess)
            return Response.builder().code(HttpStatus.NOT_FOUND.value()).data("").message("Wrong username or password").build();
        else {
            UserResponse userResponse = userResponseMapper.toDTO(user);
            return Response.builder()
                    .code(HttpStatus.OK.value())
                    .data(new LoginResponse(JWTService.genToken(userResponse)))
                    .message("Success").build();
        }
    }

}
