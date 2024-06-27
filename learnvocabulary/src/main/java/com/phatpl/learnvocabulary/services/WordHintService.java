package com.phatpl.learnvocabulary.services;

import com.phatpl.learnvocabulary.dtos.response.WordHintResponse;
import com.phatpl.learnvocabulary.filters.BaseFilter;
import com.phatpl.learnvocabulary.mappers.WordHintResponseMapper;
import com.phatpl.learnvocabulary.models.Word;
import com.phatpl.learnvocabulary.repositories.WordRepository;
import com.phatpl.learnvocabulary.utils.DefineDatatype.Pair;
import com.phatpl.learnvocabulary.utils.RadixTrie.PruningRadixTrie;
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

    public List<WordHintResponse> findByDB(String word) {
        return wordHintResponseMapper.toListDTO(wordRepository.findByWordLike(word + "%"));
    }

    public List<WordHintResponse> findByTrie(String word) {
        var ids = BuildTrie.find(word);
        var words = new ArrayList<WordHintResponse>();
        for (Integer id : ids) {
            words.add(wordHintResponseMapper.toDTO(BuildTrie.mapWords.get(id)));
        }
        return words;
    }


    public Pair<Integer, String> deCompressWord(String compressedWord) {
        Integer id = 0;
        String word = "";
        for (int i = 0; i < compressedWord.length(); i++) {
            if (Character.isLetter(compressedWord.charAt(i))) {
                word += compressedWord.charAt(i);
            } else {
                id = id * 10 + compressedWord.charAt(i) - '0';
            }
        }
        return new Pair<>(id, word);
    }

    public List<WordHintResponse> findByJPruningRadixTrie(String prefix) {
        var result = PruningRadixTrie.getTopkTermsForPrefix(prefix, 10);
        var words = new ArrayList<WordHintResponse>();
        WordHintResponse wordHintResponse = new WordHintResponse();
        for (var w : result) {
            var word = deCompressWord(w.getTerm());
            wordHintResponse.setWord(word.getSecond());
            wordHintResponse.setId(word.getFirst());
            words.add(wordHintResponse);
        }
        return words;
    }
}
