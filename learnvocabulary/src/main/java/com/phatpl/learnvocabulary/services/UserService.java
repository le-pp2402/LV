package com.phatpl.learnvocabulary.services;

import com.phatpl.learnvocabulary.dtos.request.RegisterRequest;
import com.phatpl.learnvocabulary.dtos.response.UserResponse;
import com.phatpl.learnvocabulary.mappers.RegisterRequestMapper;
import com.phatpl.learnvocabulary.mappers.UserResponseMapper;
import com.phatpl.learnvocabulary.models.User;
import com.phatpl.learnvocabulary.repositories.UserRepository;
import com.phatpl.learnvocabulary.utils.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder(8);
    }

    public User findById(Integer id) {
        var x = userRepository.findById(id);
        return (x.isPresent() ? x.get() : null);
    }

    public List<UserResponse> findAll() {
        List<User> users = userRepository.findAll();
        return UserResponseMapper.instance.toListDTO(users);
    }

    public UserResponse register(RegisterRequest request) {
        User user = RegisterRequestMapper.instance.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        var userOpt = userRepository.findByUsername(user.getUsername());
        return  UserResponseMapper.instance.toDTO(userOpt.get(0));
    }
}
