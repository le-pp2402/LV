package com.phatpl.learnvocabulary.mapper;

import com.phatpl.learnvocabulary.dto.request.RegisterRequest;
import com.phatpl.learnvocabulary.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RegisterRequestMapper extends BaseMapper<User, RegisterRequest> {
     RegisterRequestMapper instance = Mappers.getMapper(RegisterRequestMapper.class);
}
