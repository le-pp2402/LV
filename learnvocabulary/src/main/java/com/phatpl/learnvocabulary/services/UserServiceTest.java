package com.phatpl.learnvocabulary.services;

import com.phatpl.learnvocabulary.dtos.BaseDTO;
import com.phatpl.learnvocabulary.dtos.request.RegisterRequest;
import com.phatpl.learnvocabulary.dtos.response.UserResponse;
import com.phatpl.learnvocabulary.mappers.BaseMapper;
import com.phatpl.learnvocabulary.mappers.RegisterRequestMapper;
import com.phatpl.learnvocabulary.mappers.UserResponseMapper;
import com.phatpl.learnvocabulary.models.User;
import com.phatpl.learnvocabulary.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceTest extends BaseService<User, UserRepository, UserResponse> {
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserServiceTest(BaseMapper<User, UserResponse> baseMapper, UserRepository userRepository) {
        super(baseMapper, userRepository);
        this.passwordEncoder = new BCryptPasswordEncoder(8);
    }

    public UserResponse register(RegisterRequest request) {
        User user = RegisterRequestMapper.instance.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        getRepo().save(user);
        var userOpt = getRepo().findByUsername(user.getUsername());
        return  UserResponseMapper.instance.toDTO(userOpt.get(0));
    }
}
