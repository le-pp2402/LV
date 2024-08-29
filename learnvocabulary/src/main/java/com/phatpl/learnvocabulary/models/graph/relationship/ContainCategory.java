package com.phatpl.learnvocabulary.models.graph.relationship;

import com.phatpl.learnvocabulary.models.graph.NCategory;
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
@RelationshipEntity(type = "CONTAIN_CATEGORY")
public class ContainCategory {
    @Id
    @GeneratedValue
    private Long id;

    @StartNode
    NVideo video;

    @EndNode
    NCategory category;
}
