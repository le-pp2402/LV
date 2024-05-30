package com.phatpl.learnvocabulary.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BaseRepository<T> extends JpaRepository<T, Integer> {
    public Optional<T> findById(Integer id);
}
