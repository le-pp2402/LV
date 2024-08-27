package com.phatpl.learnvocabulary.repositories.jpa;

import com.phatpl.learnvocabulary.filters.ResourcesFilter;
import com.phatpl.learnvocabulary.models.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResourceRepository extends BaseRepository<Resource, ResourcesFilter, Integer> {
    Optional<Resource> findById(Integer id);

    List<Resource> findAll();
}
