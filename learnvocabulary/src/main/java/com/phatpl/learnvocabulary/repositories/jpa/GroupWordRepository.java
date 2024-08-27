package com.phatpl.learnvocabulary.repositories.jpa;

import com.phatpl.learnvocabulary.filters.BaseFilter;
import com.phatpl.learnvocabulary.models.GroupWord;

import java.util.List;
import java.util.Optional;

public interface GroupWordRepository extends BaseRepository<GroupWord, BaseFilter, Integer> {

    List<GroupWord> findByGroupId(Integer groupId);

    void deleteByGroupIdAndWordId(Integer groupId, Integer wordId);

    Optional<GroupWord> findByGroupIdAndWordId(Integer groupId, Integer wordId);
}
