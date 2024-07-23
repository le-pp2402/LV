package com.phatpl.learnvocabulary.services;

import com.phatpl.learnvocabulary.dtos.response.WordHintResponse;
import com.phatpl.learnvocabulary.filters.BaseFilter;
import com.phatpl.learnvocabulary.mappers.WordHintResponseMapper;
import com.phatpl.learnvocabulary.models.Word;
import com.phatpl.learnvocabulary.repositories.WordRepository;
import com.phatpl.learnvocabulary.utils.Trie.BuildTrie;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WordHintService extends BaseService<Word, WordHintResponse, BaseFilter, Integer> {

    private final WordRepository wordRepository;
    private final WordHintResponseMapper wordHintResponseMapper;

    public WordHintService(WordRepository wordRepository, WordHintResponseMapper wordHintResponseMapper) {
        super(wordHintResponseMapper, wordRepository);
        this.wordRepository = wordRepository;
        this.wordHintResponseMapper = wordHintResponseMapper;
    }

    public List<WordHintResponse> findByTrie(String word) {
        var ids = BuildTrie.find(word);
        var words = new ArrayList<WordHintResponse>();
        for (Integer id : ids) {
            words.add(wordHintResponseMapper.toDTO(BuildTrie.mapWords.get(id)));
        }
        return words;
    }
}
