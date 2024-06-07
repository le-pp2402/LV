package com.phatpl.learnvocabulary.repositories;

import com.phatpl.learnvocabulary.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Component
public interface UserRepository extends JpaRepository<User, Integer>, BaseRepository<User> {
    List<User> findByUsername(String username);
    List<User> findByEmail(String email);
    Optional<User> findById(Integer id);
}
