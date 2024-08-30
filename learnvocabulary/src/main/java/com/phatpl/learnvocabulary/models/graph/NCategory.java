package com.phatpl.learnvocabulary.models.graph;

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
@NoArgsConstructor
@AllArgsConstructor
@NodeEntity(label = "Category")
public class NCategory {
    @Id
    @GeneratedValue
    private Long id;

    @Property("category")
    private String category;

    public NCategory(String category) {
        this.category = category;
    }
}
