package com.phatpl.learnvocabulary.services;

import com.phatpl.learnvocabulary.repositories.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class BaseService<T, RP extends BaseRepository<T>> {
    @Autowired
    RP repo;

    List<T> findAll() {
        return repo.findAll();
    }


}
