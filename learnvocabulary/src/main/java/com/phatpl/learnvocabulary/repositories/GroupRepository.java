package com.phatpl.learnvocabulary.repositories;

import com.phatpl.learnvocabulary.filters.GroupFilter;
import com.phatpl.learnvocabulary.models.Group;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends BaseRepository<Group , GroupFilter, Integer> {
    Optional<Group> findByName(String name);

    @Query(value = "SELECT *\n" +
            "FROM (\n" +
            "\tSELECT user_group.id, user_group.group_id, user_group.is_owner \n" +
            "    FROM user_group \n" +
            "    WHERE user_id = :id\n" +
            "    ) as gug\n" +
            "\tJOIN `groups` \n" +
            "\tON `groups`.id = gug.id\n" +
            "WHERE (`groups`.is_private = 1 and gug.is_owner = 1) or (`groups`.is_private = 0)", nativeQuery = true)
    List<Group> findByUserId(@Param("id") Integer id);

}