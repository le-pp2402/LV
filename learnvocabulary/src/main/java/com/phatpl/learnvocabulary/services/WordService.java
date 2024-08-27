package com.phatpl.learnvocabulary.services;

import com.phatpl.learnvocabulary.dtos.response.WordResponse;
import com.phatpl.learnvocabulary.filters.BaseFilter;
import com.phatpl.learnvocabulary.mappers.WordResponseMapper;
import com.phatpl.learnvocabulary.models.Word;
import com.phatpl.learnvocabulary.repositories.jpa.WordRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public class WordService extends BaseService<Word, WordResponse, BaseFilter, Integer> {
    WordResponseMapper wordResponseMapper;
    WordRepository wordRepository;

    @Autowired
    public WordService(WordResponseMapper wordResponseMapper, WordRepository wordRepository) {
        super(wordResponseMapper, wordRepository);
        this.wordResponseMapper = wordResponseMapper;
        this.wordRepository = wordRepository;
    }

}
