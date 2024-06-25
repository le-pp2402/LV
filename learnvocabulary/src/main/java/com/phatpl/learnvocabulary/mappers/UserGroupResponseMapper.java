package com.phatpl.learnvocabulary.mappers;

import com.phatpl.learnvocabulary.dtos.response.UserGroupResponse;
import com.phatpl.learnvocabulary.models.UserGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserGroupResponseMapper extends BaseMapper<UserGroup, UserGroupResponse> {
    UserGroupResponseMapper instance = Mappers.getMapper(UserGroupResponseMapper.class);

    @Override
    @Mapping(source = "isOwner", target = "isOwner")
    UserGroupResponse toDTO(UserGroup entity);
}
