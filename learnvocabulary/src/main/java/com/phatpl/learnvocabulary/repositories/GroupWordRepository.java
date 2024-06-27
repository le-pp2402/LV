package com.phatpl.learnvocabulary.repositories;

import com.phatpl.learnvocabulary.filters.BaseFilter;
import com.phatpl.learnvocabulary.models.GroupWord;

import java.util.List;

public interface GroupWordRepository extends BaseRepository<GroupWord, BaseFilter, Integer> {

    List<GroupWord> findByGroupId(Integer groupId);
    
}
