package com.phatpl.learnvocabulary.mappers;

import com.phatpl.learnvocabulary.dtos.request.RegisterRequest;
import com.phatpl.learnvocabulary.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RegisterRequestMapper extends BaseMapper<User, RegisterRequest> {
     RegisterRequestMapper instance = Mappers.getMapper(RegisterRequestMapper.class);
}
