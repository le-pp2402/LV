package com.phatpl.learnvocabulary.mappers;

import com.phatpl.learnvocabulary.dtos.request.CreateGroupRequest;
import com.phatpl.learnvocabulary.models.Group;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface GroupRequestMapper extends  BaseMapper<Group, CreateGroupRequest> {
    GroupRequestMapper instance = Mappers.getMapper(GroupRequestMapper.class);
}