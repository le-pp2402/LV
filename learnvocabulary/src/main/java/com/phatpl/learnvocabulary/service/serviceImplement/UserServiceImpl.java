package com.phatpl.learnvocabulary.service.serviceImplement;

import com.phatpl.learnvocabulary.model.User;
import com.phatpl.learnvocabulary.repository.UserRepository;
import com.phatpl.learnvocabulary.repository.UserRepository;
import com.phatpl.learnvocabulary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;

    @Autowired
    public UserServiceImpl(final UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public User findById(Integer id) {
        Optional<User> user = this.userRepo.findById(id);
        return (user.isPresent() ? user.get() : null);
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) {
        Optional<User> user = this.userRepo.findByUsernameAndPassword(username, password);
        return (user.isPresent() ? user.get() : null);
    }

}
