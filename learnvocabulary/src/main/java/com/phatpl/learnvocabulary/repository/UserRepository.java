package com.phatpl.learnvocabulary.repository;

import com.phatpl.learnvocabulary.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public Optional<User> findByUsernameAndPassword(String username, String password);
}
