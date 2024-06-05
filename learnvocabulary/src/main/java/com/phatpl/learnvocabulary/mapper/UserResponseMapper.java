package com.phatpl.learnvocabulary.mapper;

import com.phatpl.learnvocabulary.dto.response.UserResponse;
import com.phatpl.learnvocabulary.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserResponseMapper extends BaseMapper<User, UserResponse> {
    UserResponseMapper instance = Mappers.getMapper(UserResponseMapper.class);
}
