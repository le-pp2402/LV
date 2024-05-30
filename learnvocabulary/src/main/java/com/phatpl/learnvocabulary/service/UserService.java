package com.phatpl.learnvocabulary.service;

import com.phatpl.learnvocabulary.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    public User findById(Integer id);
    public User findByUsernameAndPassword(String username, String password);
}
