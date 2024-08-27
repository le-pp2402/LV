package com.phatpl.learnvocabulary.models.graph;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@NodeEntity(label = "User")
public class NUser {
    @Id
    @GeneratedValue
    private Long id;

    @Property(name = "username")
    @NotNull
    private String username;

    @Property(name = "email")
    @NotNull
    String email;

    @Property(name = "isAdmin")
    @NotNull
    Boolean isAdmin = false;

    @Property(name = "elo")
    @NotNull
    Long elo = 0L;

}
