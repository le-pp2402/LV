package com.phatpl.learnvocabulary.repositories.graph;

import com.phatpl.learnvocabulary.filters.BaseFilter;
import com.phatpl.learnvocabulary.models.graph.relationship.Friendship;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EFriendshipRepo extends BaseRepo<Friendship, BaseFilter, Long> {

    @Query("""
            MATCH (n) WHERE n.user_id = $user_id_1
            WITH (n)
            MATCH (m) WHERE m.user_id = $user_id_2
            MATCH (n) - [p: IS_FRIEND] -> (m)
            RETURN ID(p)
            """
    )
    Long findFriendship(Long user_id_1, Long user_id_2);

    Optional<Friendship> findById(Long id);

    Friendship save(@NotNull Friendship friendship);

    void deleteById(@NotNull Long id);

}
