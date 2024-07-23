package com.phatpl.learnvocabulary.repositories;

import com.phatpl.learnvocabulary.filters.GroupFilter;
import com.phatpl.learnvocabulary.models.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends BaseRepository<Group, GroupFilter, Integer> {

    Optional<Group> findById(Integer id);

    @Query(value = """
            SELECT `groups`.*
            FROM (
            \tSELECT *\s
                FROM user_group\s
                WHERE user_id = :userId
                ) as gug
            \tJOIN `groups`\s
            \tON `groups`.id = gug.group_id
            WHERE (`groups`.is_private = 1 and gug.is_owner = 1) or (`groups`.is_private = 0)""", nativeQuery = true)
    List<Group> findByUserId(@Param("userId") Integer userId);

    Page<Group> findAll(Pageable pageable);
}