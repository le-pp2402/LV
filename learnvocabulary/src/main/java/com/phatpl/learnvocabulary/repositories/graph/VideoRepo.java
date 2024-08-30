package com.phatpl.learnvocabulary.repositories.graph;

import com.phatpl.learnvocabulary.filters.BaseFilter;
import com.phatpl.learnvocabulary.models.graph.NVideo;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoRepo extends BaseRepo<NVideo, BaseFilter, Long> {

    Optional<NVideo> findByVideoId(Long videoId);

    @Query("""
            MATCH (u: User) - [f: IS_FRIEND] - (uf: User) - [l: LIKE] -> (v: Video)
            WHERE f.status = 1 AND u.user_id = $userId
            WITH v, rand() AS rd
            ORDER BY rd
            LIMIT 10
            RETURN v
            """)
    List<NVideo> getSuggestionFromFriends(Long userId);

    @Query("""
            MATCH (v: Video) - [p:CONTAIN_CATEGORY] - (c: Category)
            WHERE ID(c) in $categoryId
            WITH v, rand() AS rd
            ORDER BY rd
            LIMIT 10
            RETURN v
            """)
    List<NVideo> getSuggestionFromCategory(List<Long> categoryId);
}
