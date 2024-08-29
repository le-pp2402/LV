package com.phatpl.learnvocabulary.repositories.graph;

import com.phatpl.learnvocabulary.filters.BaseFilter;
import com.phatpl.learnvocabulary.models.graph.NCategory;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo extends BaseRepo<NCategory, BaseFilter, Long> {
}
