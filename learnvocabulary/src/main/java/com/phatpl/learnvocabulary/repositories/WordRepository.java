package com.phatpl.learnvocabulary.repositories;

import com.phatpl.learnvocabulary.filters.BaseFilter;
import com.phatpl.learnvocabulary.models.Word;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordRepository extends BaseRepository<Word, BaseFilter, Integer> {

    @Query(value = "select * from words where word like :p limit 10", nativeQuery = true)
    List<Word> findByWordLike(@Param("p") String pattern);
}
