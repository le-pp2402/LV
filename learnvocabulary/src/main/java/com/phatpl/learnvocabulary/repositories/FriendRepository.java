package com.phatpl.learnvocabulary.repositories;


import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.AccessMode;
import org.neo4j.driver.Driver;
import org.neo4j.driver.SessionConfig;
import org.neo4j.driver.Values;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class FriendRepository {

    private final Driver neo4jDriver;

    @NotNull
    @Value("${NEO4J_DB}")
    private String database;

    @Autowired
    public FriendRepository(Driver neo4jDriver) {
        this.neo4jDriver = neo4jDriver;
    }

    public void createUser(int id) {
        String stm = """
                    MERGE (a:User {user_id: $id})
                """;
        var sessionCfg = SessionConfig.builder().withDatabase(database).withDefaultAccessMode(AccessMode.WRITE).build();
        try (var session = neo4jDriver.session(sessionCfg)) {
            session.executeWrite(tx -> tx.run(stm, Values.value(Map.of("id", id))).consume());
        }
    }


    public boolean isExistedFriendRequest(Integer fromUserID, Integer toUserID) {
        String stm = """
                    MATCH (
                            a: User {
                                user_id: $fromUserID
                            }
                          ) -[p:IS_FRIEND]->
                          (
                            b: User {
                                user_id: $toUserID
                            }
                          )
                          RETURN COUNT(p) AS ans
                """;
        var sessionCfg = SessionConfig.builder().withDatabase(database).withDefaultAccessMode(AccessMode.READ).build();
        try (var session = neo4jDriver.session(sessionCfg)) {
            var param = new HashMap<>();
            param.put("fromUserID", fromUserID);
            param.put("toUserID", toUserID);
            var result = session.executeWrite(tx -> tx.run(stm, Values.value(param)).single());
            return result.get("ans").asInt() > 0;
        }
    }

    public void makeFriend(Integer fromUserID, Integer toUserID) {
        String stm = """
                    MATCH (a:User { user_id: $fromUserID }) - [p : IS_FRIEND] -> (b: User { user_id: $toUserID })
                    SET p.status = 1
                    RETURN a, b
                """;
        var param = new HashMap<>();
        param.put("fromUserID", fromUserID);
        param.put("toUserID", toUserID);

        var sessionCfg = SessionConfig.builder().withDatabase(database).withDefaultAccessMode(AccessMode.WRITE).build();
        try (var session = neo4jDriver.session(sessionCfg)) {
            session.executeWrite(tx -> tx.run(stm, Values.value(param)).consume());
        }
    }

    public void createFriendRequest(Integer fromUserID, Integer toUserID) {
        if (isExistedFriendRequest(fromUserID, toUserID)) return;
        if (isExistedFriendRequest(toUserID, fromUserID)) {
            makeFriend(toUserID, fromUserID);
            return;
        }
        String stm = """
                    MATCH (a:User { user_id: $fromUserID }), (b: User { user_id: $toUserID })
                    MERGE (a) - [p : IS_FRIEND { status : 0 }] -> (b)
                    RETURN a, b
                """;
        var param = new HashMap<>();
        param.put("fromUserID", fromUserID);
        param.put("toUserID", toUserID);

        var sessionCfg = SessionConfig.builder().withDatabase(database).withDefaultAccessMode(AccessMode.WRITE).build();
        try (var session = neo4jDriver.session(sessionCfg)) {
            session.executeWrite(tx -> tx.run(stm, Values.value(param)).consume());
        }
    }

    public List<Integer> findRequestFriend(Integer userID) {
        String stm = """
                    MATCH (a:User {user_id: $userID}) <- [p:IS_FRIEND {status: 0}] - (b: User)
                    RETURN a
                """;
        var param = new HashMap<>();
        param.put("userID", userID);

        var friendRequests = new ArrayList<Integer>();

        var sessionCfg = SessionConfig.builder().withDatabase(database).withDefaultAccessMode(AccessMode.READ).build();
        try (var session = neo4jDriver.session(sessionCfg)) {
            var result = session.executeRead(tx -> tx.run(stm, Values.value(param)).list());
            if (result != null) {
                for (var elem : result) {
                    friendRequests.add(elem.get("user_id").asInt());
                }
            }
        }

        return friendRequests;
    }

    public List<Integer> findFriends(Integer userID) {
        String stm = """
                MATCH (a: User {user_id: $userID}) - [p:IS_FRIEND {status: 1}] - (b: User)
                RETURN b
                """;
        var param = new HashMap<>();
        param.put("userID", userID);

        var friends = new ArrayList<Integer>();
        var sessionCfg = SessionConfig.builder().withDatabase(database).withDefaultAccessMode(AccessMode.READ).build();

        try (var session = neo4jDriver.session(sessionCfg)) {
            var result = session.executeRead(tx -> tx.run(stm, Values.value(param)).list());
            if (result != null) {
                for (var elem : result) {
                    friends.add(elem.values().get(0).get("user_id").asInt());
                }
            }
        }
        return friends;
    }

    public List<Integer> getFriendRecommends(Integer userID) {
        String stm = """
                    MATCH ( n:User { user_id: $userID })
                    MATCH  (n)-[:IS_FRIEND*..3]-(m)
                    WHERE NOT (n)-[:IS_FRIEND]-(m)
                    RETURN m LIMIT 20
                """;
        var param = new HashMap<>();
        param.put("userID", userID);

        var results = new ArrayList<Integer>();
        var sessionCfg = SessionConfig.builder().withDatabase(database).withDefaultAccessMode(AccessMode.READ).build();

        try (var session = neo4jDriver.session(sessionCfg)) {
            var resultSet = session.executeRead(tx -> tx.run(stm, Values.value(param)).list());
            if (resultSet != null) {
                for (var elem : resultSet) {
                    var id = elem.values().get(0).get("user_id").asInt();
                    if (id != userID)
                        results.add(id);
                }
            }
        }

        return results;
    }

    public void refuseFriendRequest(Integer userID1, Integer userID2) {
        String stm = """
                MATCH (a: User {user_id : $userID1 } )-[p:IS_FRIEND]-(b : User { user_id: $userID2 } )
                DETACH DELETE p
                RETURN a, b
                """;
        var param = new HashMap<>();
        param.put("userID1", userID1);
        param.put("userID2", userID2);
        var sessionCfg = SessionConfig.builder().withDatabase(database).withDefaultAccessMode(AccessMode.WRITE).build();

        try (var session = neo4jDriver.session(sessionCfg)) {
            session.executeWrite(tx -> tx.run(stm, Values.value(param)).consume());
        }
    }
}


