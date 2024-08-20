//package com.phatpl.learnvocabulary.models.neo4j.node;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.springframework.data.neo4j.core.schema.*;
//
//import java.util.List;
//
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@Node(primaryLabel = "User")
//public class User {
//    @Id
//    @GeneratedValue
//    private Integer id;
//
//    @Property(name = "username")
//    private String username;
//
//    @Relationship(type = "IS_FRIEND", direction = Relationship.Direction.OUTGOING)
//    List<User> friends;
//
//}
