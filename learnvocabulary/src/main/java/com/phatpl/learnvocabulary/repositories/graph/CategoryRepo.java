package com.phatpl.learnvocabulary.repositories.graph;

import com.phatpl.learnvocabulary.filters.BaseFilter;
import com.phatpl.learnvocabulary.models.graph.NCategory;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepo extends BaseRepo<NCategory, BaseFilter, Long> {

    @Query("""
            MATCH(v : Video)-[p : ContainCategoryRepo]->(c : Category)
            WHERE v.video_id = $videoId
            RETURN c
            """)
    List<NCategory> findByVideoId(Long videoId);
}
