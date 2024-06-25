package com.phatpl.learnvocabulary.repositories;

import com.phatpl.learnvocabulary.dtos.response.UserGroupResponse;
import com.phatpl.learnvocabulary.filters.BaseFilter;
import com.phatpl.learnvocabulary.models.UserGroup;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserGroupRepository extends BaseRepository<UserGroup, BaseFilter, Integer> {
    Optional<UserGroup> findById(Integer id);

    List<UserGroupResponse> findByUserId(Integer id);

    @Query(value = "select count(user_id) from user_group where user_id = :id", nativeQuery = true)
    Integer numberOfGroups(@Param("id") Integer userId);

    Optional<UserGroup> findByUserIdAndGroupId(Integer userId, Integer groupId);

    void deleteByUserIdAndGroupId(Integer userId, Integer groupId);

    void deleteByGroupId(Integer groupId);
}
