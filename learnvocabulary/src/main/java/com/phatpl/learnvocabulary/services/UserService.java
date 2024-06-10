package com.phatpl.learnvocabulary.services;

import com.phatpl.learnvocabulary.dtos.request.RegisterRequest;
import com.phatpl.learnvocabulary.dtos.response.UserResponse;
import com.phatpl.learnvocabulary.filters.UserFilter;
import com.phatpl.learnvocabulary.mappers.RegisterRequestMapper;
import com.phatpl.learnvocabulary.mappers.UserResponseMapper;
import com.phatpl.learnvocabulary.models.User;
import com.phatpl.learnvocabulary.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseService<User, UserResponse, UserFilter, Integer> {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    @Autowired
    public UserService(UserResponseMapper userResponseMapper, UserRepository userRepository) {
        super(userResponseMapper, userRepository);
        this.passwordEncoder = new BCryptPasswordEncoder(8);
        this.userRepository = userRepository;
    }

    public UserResponse register(RegisterRequest request) {
        User user = RegisterRequestMapper.instance.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        var userOpt = userRepository.findByUsername(user.getUsername());
        return UserResponseMapper.instance.toDTO(userOpt.get(0));
    }
}
