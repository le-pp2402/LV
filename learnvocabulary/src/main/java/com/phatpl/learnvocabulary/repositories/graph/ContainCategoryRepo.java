package com.phatpl.learnvocabulary.repositories.graph;

import com.phatpl.learnvocabulary.filters.BaseFilter;
import com.phatpl.learnvocabulary.models.graph.relationship.ContainCategory;
import org.springframework.data.neo4j.annotation.Query;

import java.util.Optional;

public interface ContainCategoryRepo extends BaseRepo<ContainCategory, BaseFilter, Long> {
    @Query("""
            MATCH(v:Video)-[p: ContainCategoryRepo]->(c:Category)
            WHERE p.video_id = $videoId AND ID(c) = $categoryId
            return (p)
            """)
    Optional<ContainCategory> findByVideoIdAndCategoryId(Long videoId, Long categoryId);
}
