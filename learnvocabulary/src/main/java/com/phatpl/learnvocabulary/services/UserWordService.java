package com.phatpl.learnvocabulary.services;

import com.phatpl.learnvocabulary.dtos.request.ResultRequest;
import com.phatpl.learnvocabulary.dtos.request.WordsDeleteRequest;
import com.phatpl.learnvocabulary.dtos.request.WordsSaveRequest;
import com.phatpl.learnvocabulary.dtos.response.UserWordResponse;
import com.phatpl.learnvocabulary.exceptions.UnauthorizationException;
import com.phatpl.learnvocabulary.filters.BaseFilter;
import com.phatpl.learnvocabulary.mappers.UserWordResponseMapper;
import com.phatpl.learnvocabulary.models.User;
import com.phatpl.learnvocabulary.models.UserWord;
import com.phatpl.learnvocabulary.repositories.UserRepository;
import com.phatpl.learnvocabulary.repositories.UserWordRepository;
import com.phatpl.learnvocabulary.repositories.WordRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public class UserWordService extends BaseService<UserWord, UserWordResponse, BaseFilter, Integer> {

    UserWordRepository userWordRepository;
    UserWordResponseMapper userWordResponseMapper;
    UserRepository userRepository;
    WordRepository wordRepository;
    UserService userService;

    @Autowired
    public UserWordService(UserWordRepository userWordRepository, UserWordResponseMapper userWordResponseMapper, UserRepository userRepository, WordRepository wordRepository, UserService userService) {
        super(userWordResponseMapper, userWordRepository);
        this.userWordRepository = userWordRepository;
        this.userWordResponseMapper = userWordResponseMapper;
        this.userRepository = userRepository;
        this.wordRepository = wordRepository;
        this.userService = userService;
    }

    public UserWord getByUserAndWordId(User user, Integer wordId) {
        var userWord = userWordRepository.findByUserIdAndWordId(user.getId(), wordId).orElseGet(UserWord::new);
        var word = wordRepository.findById(wordId);
        if (word.isEmpty()) return null;
        if (userWord.getId() == null) {
            userWord.setUser(user);
            userWord.setWord(word.get());
            userWord.setRank(1);
        }
        return userWordRepository.save(userWord);
    }

    public boolean saveWords(WordsSaveRequest request) {
        var userId = userService.extractUserId();
        var user = userRepository.findById(userId).orElseThrow(UnauthorizationException::new);
        var words = request.getWords();
        for (var id : words) {
            getByUserAndWordId(user, id);
        }
        return true;
    }


    public boolean deleteWords(WordsDeleteRequest request) {
        var userId = userService.extractUserId();
        userWordRepository.deleteByUserIdAndWordIdIn(userId, request.getWords());
        return true;
    }

    public List<UserWordResponse> answer(ResultRequest request) {
        var userId = userService.extractUserId();
        var user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);

        Map<Integer, Boolean> wordId = new HashMap<>();
        for (var result : request.getAnswers()) {
            var userWord = getByUserAndWordId(user, result.get(0));
            if (userWord == null) {
                continue;
            }
            if (result.get(1) == 1) {
                userWord.AnswerCorrect();
            } else {
                userWord.AnswerWrong();
            }
            wordId.put(result.get(0), true);
        }
        var wordsId = new ArrayList<Integer>();
        wordId.forEach((key, value) -> wordsId.add(key));
        var response = userWordRepository.findAllByUserIdAndWordIdIn(userId, wordsId);
        return userWordResponseMapper.toListDTO(response);
    }

    public List<UserWordResponse> getWordsOfUser() {
        var userId = userService.extractUserId();
        var words = userWordRepository.findByUserId(userId);
        return UserWordResponseMapper.instance.toListDTO(words);
    }


    public UserWordResponse getWordOfUser(Integer wordId) {
        var userId = userService.extractUserId();
        var userWord = userWordRepository.findByUserIdAndWordId(userId, wordId).orElseThrow(EntityNotFoundException::new);
        return userWordResponseMapper.toDTO(userWord);
    }
}