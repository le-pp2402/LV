package com.phatpl.learnvocabulary.repositories;

import org.springframework.data.domain.Page;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface BaseRepository<T> {
    List<T> findAll();
    Optional<T> findById(Integer id);
    void deleteById(Integer id);
    T save(T e);
}
