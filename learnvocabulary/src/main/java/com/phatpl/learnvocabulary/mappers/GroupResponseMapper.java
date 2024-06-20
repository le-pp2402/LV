package com.phatpl.learnvocabulary.mappers;

import com.phatpl.learnvocabulary.dtos.response.GroupResponse;
import com.phatpl.learnvocabulary.models.Group;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface GroupResponseMapper extends BaseMapper<Group, GroupResponse> {
    GroupResponseMapper instance = Mappers.getMapper(GroupResponseMapper.class);
}