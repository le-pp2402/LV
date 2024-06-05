package com.phatpl.learnvocabulary.repository;

import com.phatpl.learnvocabulary.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Component
public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findByUsername(String username);
    List<User> findByEmail(String email);
    Optional<User> findById(Integer Id);
    User save(User user);
}
