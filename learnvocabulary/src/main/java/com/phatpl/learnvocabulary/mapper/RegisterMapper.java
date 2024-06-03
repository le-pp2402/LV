package com.phatpl.learnvocabulary.mapper;

import com.phatpl.learnvocabulary.dto.request.RegisterRequest;
import com.phatpl.learnvocabulary.model.User;
import org.springframework.stereotype.Service;

@Service
public class RegisterMapper implements BaseMapper<RegisterRequest, User> {

    @Override
    public User convert(RegisterRequest request) {
        return User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .email(request.getEmail())
                .elo(0)
                .isAdmin(false)
                .build();
    }
}
