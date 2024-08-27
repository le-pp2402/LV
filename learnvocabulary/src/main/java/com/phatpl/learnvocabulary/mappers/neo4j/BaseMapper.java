package com.phatpl.learnvocabulary.mappers.neo4j;

import java.util.List;

public interface BaseMapper<T, G> {
    G toGraphModel(T t);

    T toTableModel(G g);

    List<G> toGraphModels(List<T> ts);

    List<T> toTableModels(List<G> gs);
}
