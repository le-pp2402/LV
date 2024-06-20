package com.phatpl.learnvocabulary.repositories;

import com.phatpl.learnvocabulary.filters.GroupFilter;
import com.phatpl.learnvocabulary.models.Group;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends BaseRepository<Group , GroupFilter, Integer> {
    Optional<Group> findByName(String name);
}