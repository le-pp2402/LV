package com.phatpl.learnvocabulary.mappers;

import com.phatpl.learnvocabulary.dtos.response.WordHintResponse;
import com.phatpl.learnvocabulary.models.Word;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface WordHintResponseMapper extends BaseMapper<Word, WordHintResponse> {
    WordHintResponseMapper instance = Mappers.getMapper(WordHintResponseMapper.class);
}
