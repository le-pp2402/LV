package com.phatpl.learnvocabulary.mappers;

import com.phatpl.learnvocabulary.dtos.response.UserWordResponse;
import com.phatpl.learnvocabulary.models.UserWord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserWordResponseMapper extends BaseMapper<UserWord, UserWordResponse> {
    UserWordResponseMapper instance = Mappers.getMapper(UserWordResponseMapper.class);

    @Mapping(source = "word.id", target = "wordId")
    @Mapping(source = "word.word", target = "word")
    UserWordResponse toDTO(UserWord entity);

    @Mapping(source = "wordId", target = "word.id")
    @Mapping(source = "word", target = "word.word")
    UserWord toEntity(UserWordResponse dto);

    List<UserWordResponse> toListDTO(List<UserWord> e);

    List<UserWordResponse> toListDTO(Page<UserWord> e);

    List<UserWord> toListEntity(List<UserWordResponse> dto);
}
