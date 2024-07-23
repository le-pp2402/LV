package com.phatpl.learnvocabulary.repositories;

import com.phatpl.learnvocabulary.filters.BaseFilter;
import com.phatpl.learnvocabulary.models.UserWord;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// https://stackoverflow.com/questions/64762080/how-to-map-sql-native-query-result-into-dto-in-spring-jpa-repository
public interface UserWordRepository extends BaseRepository<UserWord, BaseFilter, Integer> {


    Optional<UserWord> findByUserIdAndWordId(Integer userId, Integer id);

    List<UserWord> findByUserId(Integer userId);

    void deleteByUserIdAndWordIdIn(Integer userId, List<Integer> word);

    List<UserWord> findAllByUserIdAndWordIdIn(Integer userId, ArrayList<Integer> wordsId);
}
