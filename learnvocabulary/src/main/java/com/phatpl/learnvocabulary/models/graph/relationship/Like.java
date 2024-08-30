package com.phatpl.learnvocabulary.models.graph.relationship;

import com.phatpl.learnvocabulary.models.graph.NUser;
import com.phatpl.learnvocabulary.models.graph.NVideo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.neo4j.ogm.annotation.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RelationshipEntity(type = "LIKE")
public class Like {
    @Id
    @GeneratedValue
    private Long id;

    @StartNode
    NUser user;

    @EndNode
    NVideo video;
}
