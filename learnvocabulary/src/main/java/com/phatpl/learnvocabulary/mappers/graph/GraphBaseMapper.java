package com.phatpl.learnvocabulary.mappers.graph;

import java.util.List;

public interface GraphBaseMapper<T, G> {
    G toGraphModel(T t);

    T toTableModel(G g);

    List<G> toGraphModels(List<T> ts);

    List<T> toTableModels(List<G> gs);
}
