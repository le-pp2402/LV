package com.phatpl.learnvocabulary.service;

import com.phatpl.learnvocabulary.dto.request.RegisterRequest;
import com.phatpl.learnvocabulary.dto.response.UserResponse;
import com.phatpl.learnvocabulary.mapper.RegisterMapper;
import com.phatpl.learnvocabulary.mapper.UserMapper;
import com.phatpl.learnvocabulary.model.User;
import com.phatpl.learnvocabulary.repository.UserRepository;
import com.phatpl.learnvocabulary.util.Regex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RegisterMapper registerMapper;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       RegisterMapper registerMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.registerMapper = registerMapper;
        this.passwordEncoder = new BCryptPasswordEncoder(8);
    }

    public User findById(Integer id) {
        var x = userRepository.findById(id);
        return (x.isPresent() ? x.get() : null);
    }

//    public User findByUsernameAndPassword(String username, String password) {
//        var x = userRepository.findByUsernameAndPassword(username, password);
//        return (x.isPresent() ? x.get() : null);
//    }

    public List<UserResponse> findAll() {
        List<User> users = userRepository.findAll();
        return userMapper.convert(users);
    }

    public ResponseEntity<?> register(RegisterRequest request) {
        String bad = "";
        if (!Regex.checkRegex(request.getUsername(), Regex.REGEX_USERNAME)) {
            bad = "invalid username";
        } else if (!Regex.checkRegex(request.getPassword(), Regex.REGEX_PASSWORD)) {
            bad = "invalid password";
        } else if (!Regex.checkRegex(request.getEmail(), Regex.REGEX_EMAIL)) {
            bad = "invalid email";
        } else if (!userRepository.findByEmail(request.getEmail()).isEmpty()) {
            bad = "existed email";
        } else if (!userRepository.findByUsername(request.getUsername()).isEmpty()) {
            bad = "existed username";
        }
        if (!bad.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(bad);

        User user = registerMapper.convert(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).body(userMapper.convert(user));
    }
}
