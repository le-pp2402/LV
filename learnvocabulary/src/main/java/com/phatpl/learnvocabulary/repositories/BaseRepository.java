package com.phatpl.learnvocabulary.repositories;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface BaseRepository<E> {
    List<E> findAll();
    Optional<E> findById();
}
