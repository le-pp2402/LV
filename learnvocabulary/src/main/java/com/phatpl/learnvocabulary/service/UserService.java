package com.phatpl.learnvocabulary.service;

import com.phatpl.learnvocabulary.dto.request.RegisterRequest;
import com.phatpl.learnvocabulary.dto.response.UserResponse;
import com.phatpl.learnvocabulary.mapper.UserMapper;
import com.phatpl.learnvocabulary.model.User;
import com.phatpl.learnvocabulary.repository.UserRepository;
import com.phatpl.learnvocabulary.util.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = new BCryptPasswordEncoder(8);
    }

    public User findById(Integer id) {
        var x = userRepository.findById(id);
        return (x.isPresent() ? x.get() : null);
    }

    public List<UserResponse> findAll() {
        List<User> users = userRepository.findAll();
        return userMapper.userToUserResponse(users);
    }

    public UserResponse register(RegisterRequest request) {
        User user = UserMapper.instance.registerRequestToUser(request);
        if (user == null) {
            Logger.log("null @@@@@@@@@@@@@@@");
        } else {
//            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user);
        return  userMapper.userToUserResponse(user);
    }
}
