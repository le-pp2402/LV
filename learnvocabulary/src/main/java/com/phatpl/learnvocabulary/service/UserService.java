package com.phatpl.learnvocabulary.service;

import com.phatpl.learnvocabulary.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User findById(Integer id);
    User findByUsernameAndPassword(String username, String password);
}
