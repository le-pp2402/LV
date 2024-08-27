package com.phatpl.learnvocabulary.repositories.graph;

import com.phatpl.learnvocabulary.filters.BaseFilter;
import com.phatpl.learnvocabulary.models.graph.NUser;
import com.phatpl.learnvocabulary.utils.DefineDatatype.Pair;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends BaseRepo<NUser, BaseFilter, Long> {

    Optional<NUser> findByUserId(Long userID);

    Optional<NUser> findByUsername(String username);

    Optional<Pair<NUser, Long>> findByEmail(String email);

    @Query("""
            MATCH (a: User) - [p:IS_FRIEND] - (b: User)
            WHERE a.user_id = $userId AND p.status = 1
            RETURN b
            """)
    List<NUser> getFriends(Long userId);

    @Query("""
            MATCH (n :User)
            WHERE n.user_id = $userId
            MATCH (n)-[:IS_FRIEND*..3]-(m)
            WHERE NOT (n)-[:IS_FRIEND]-(m)
            RETURN m LIMIT 20
            """)
    List<NUser> suggestFriends(Long userId);

    @Query("""
            MATCH (n: User)
            WHERE n.user_id = $userId
            MATCH (m) - [p:IS_FRIEND] -> (n)
            WHERE p.status = 0
            RETURN (m)
            """)
    List<NUser> getFriendRequests(Long userId);

}
