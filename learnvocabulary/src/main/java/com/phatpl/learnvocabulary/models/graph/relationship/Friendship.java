package com.phatpl.learnvocabulary.models.graph.relationship;

import com.phatpl.learnvocabulary.models.graph.NUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.neo4j.ogm.annotation.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RelationshipEntity(type = "IS_FRIEND")
public class Friendship {
    @Id
    @GeneratedValue
    private Long id;

    @Property(name = "status")
    @NotNull
    private Long status;

    @StartNode
    @NotNull
    private NUser fromNUser;

    @EndNode
    @NotNull
    private NUser toNUser;
}
