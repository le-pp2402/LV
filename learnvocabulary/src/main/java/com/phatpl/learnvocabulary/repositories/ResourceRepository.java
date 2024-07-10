package com.phatpl.learnvocabulary.repositories;

import com.phatpl.learnvocabulary.filters.BaseFilter;
import com.phatpl.learnvocabulary.models.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResourceRepository extends BaseRepository<Resource, BaseFilter, Integer> {
    Optional<Resource> findById(Integer id);

    List<Resource> findAll();
}
