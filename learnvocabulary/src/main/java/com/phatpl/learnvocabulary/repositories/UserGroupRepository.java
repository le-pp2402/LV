package com.phatpl.learnvocabulary.repositories;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.phatpl.learnvocabulary.filters.BaseFilter;
import com.phatpl.learnvocabulary.models.UserGroup;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserGroupRepository extends BaseRepository<UserGroup, BaseFilter, Integer> {
    Optional<UserGroup> findById(Integer id);
}
