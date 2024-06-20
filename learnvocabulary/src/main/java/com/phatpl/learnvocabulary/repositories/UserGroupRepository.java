//package com.phatpl.learnvocabulary.repositories;
//
//import com.phatpl.learnvocabulary.filters.BaseFilter;
//import com.phatpl.learnvocabulary.models.UserGroup;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface UserGroupRepository extends BaseRepository<UserGroup, BaseFilter, Integer> {
//    Optional<UserGroup> findById(Integer id);
//    @Query("select u from user_group where user_id = ?1")
//    List<UserGroup> findByUser(Integer userId);
//}
