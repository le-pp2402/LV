package com.phatpl.learnvocabulary.repositories;

import com.phatpl.learnvocabulary.filters.BaseFilter;
import com.phatpl.learnvocabulary.models.Word;
import org.springframework.stereotype.Repository;

@Repository
public interface WordRepository extends BaseRepository<Word, BaseFilter, Integer> {
//    @Query(value = "SELECT words.*\n" +
//            "FROM \n" +
//            "words JOIN (SELECT group_id, word_id FROM group_word WHERE group_id = :groupId) AS gw\n" +
//            "ON gw.word_id = words.id;", nativeQuery = true)
//    List<Word> findByGroupId(@Param("groupID") Integer groupId);

}
