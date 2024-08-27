package com.phatpl.learnvocabulary.repositories.graph;


import com.phatpl.learnvocabulary.filters.BaseFilter;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepo<T, FT extends BaseFilter, ID extends Long> extends Neo4jRepository<T, ID> {
    Optional<T> findById(@NotNull ID id);

    List<T> findAll();
}

