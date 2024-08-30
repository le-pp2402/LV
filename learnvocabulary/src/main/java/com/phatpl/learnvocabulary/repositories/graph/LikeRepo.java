package com.phatpl.learnvocabulary.repositories.graph;

import com.phatpl.learnvocabulary.filters.BaseFilter;
import com.phatpl.learnvocabulary.models.graph.relationship.Like;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepo extends BaseRepo<Like, BaseFilter, Long> {
    Like save(@NotNull Like like);
}
