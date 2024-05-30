package com.phatpl.learnvocabulary.service;

import com.phatpl.learnvocabulary.model.User;
import com.phatpl.learnvocabulary.repository.BaseRepository;

import java.util.Optional;

public class UserService {
    public static BaseRepository<User> userRepo;
    public static User findById(int id) {
        Optional<User> userOpt = userRepo.findById(id);
        return (userOpt.isPresent() ? userOpt.get() : null);
    }
}
