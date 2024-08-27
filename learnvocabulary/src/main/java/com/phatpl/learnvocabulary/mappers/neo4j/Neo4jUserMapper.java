package com.phatpl.learnvocabulary.mappers.neo4j;

import com.phatpl.learnvocabulary.models.User;
import com.phatpl.learnvocabulary.models.graph.NUser;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface Neo4jUserMapper extends BaseMapper<User, NUser> {
}
