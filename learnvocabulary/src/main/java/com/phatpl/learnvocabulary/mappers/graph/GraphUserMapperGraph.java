package com.phatpl.learnvocabulary.mappers.graph;

import com.phatpl.learnvocabulary.models.User;
import com.phatpl.learnvocabulary.models.graph.NUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface GraphUserMapperGraph extends GraphBaseMapper<User, NUser> {

    @Override
    @Mapping(source = "id", target = "userId")
    @Mapping(target = "id", ignore = true)
    NUser toGraphModel(User t);
}
