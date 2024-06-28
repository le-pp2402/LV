package com.phatpl.learnvocabulary.mappers;


import com.phatpl.learnvocabulary.dtos.response.GroupWordResponse;
import com.phatpl.learnvocabulary.models.GroupWord;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface GroupWordResponseMapper extends BaseMapper<GroupWord, GroupWordResponse> {
    GroupWordResponseMapper instance = Mappers.getMapper(GroupWordResponseMapper.class);
}
