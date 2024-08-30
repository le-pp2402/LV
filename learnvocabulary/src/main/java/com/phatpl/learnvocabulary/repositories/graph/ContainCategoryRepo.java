package com.phatpl.learnvocabulary.repositories.graph;

import com.phatpl.learnvocabulary.filters.BaseFilter;
import com.phatpl.learnvocabulary.models.graph.relationship.ContainCategory;
import org.springframework.data.neo4j.annotation.Query;

import java.util.Optional;

public interface ContainCategoryRepo extends BaseRepo<ContainCategory, BaseFilter, Long> {
    @Query("""
            MATCH (v:Video) - [p: CONTAIN_CATEGORY] - (c:Category)
            WHERE v.video_id = $videoId AND ID(c) = $categoryId
            RETURN ID(p)
            """)
    Optional<Long> findByVideoIdAndCategoryId(Long videoId, Long categoryId);
}
