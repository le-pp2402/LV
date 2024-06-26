package com.phatpl.learnvocabulary.mappers;

import com.phatpl.learnvocabulary.dtos.response.WordResponse;
import com.phatpl.learnvocabulary.models.Word;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface WordResponseMapper extends BaseMapper<Word, WordResponse> {
    WordResponseMapper instance = Mappers.getMapper(WordResponseMapper.class);
}
