package com.phatpl.learnvocabulary.services;

import com.phatpl.learnvocabulary.dtos.request.SaveWordRequest;
import com.phatpl.learnvocabulary.dtos.response.WordResponse;
import com.phatpl.learnvocabulary.exceptions.UnauthorizationException;
import com.phatpl.learnvocabulary.filters.BaseFilter;
import com.phatpl.learnvocabulary.mappers.WordResponseMapper;
import com.phatpl.learnvocabulary.models.Group;
import com.phatpl.learnvocabulary.models.GroupWord;
import com.phatpl.learnvocabulary.models.Word;
import com.phatpl.learnvocabulary.repositories.GroupWordRepository;
import com.phatpl.learnvocabulary.repositories.WordRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WordService extends BaseService<Word, WordResponse, BaseFilter, Integer> {
    WordResponseMapper wordResponseMapper;
    WordRepository wordRepository;
    UserGroupService userGroupService;
    GroupWordRepository groupWordRepository;

    @Autowired
    public WordService(WordResponseMapper wordResponseMapper, WordRepository wordRepository, UserGroupService userGroupService, GroupWordRepository groupWordRepository) {
        super(wordResponseMapper, wordRepository);
        this.wordResponseMapper = wordResponseMapper;
        this.wordRepository = wordRepository;
        this.userGroupService = userGroupService;
        this.groupWordRepository = groupWordRepository;
    }

    public Boolean saveIntoGroup(SaveWordRequest request, JwtAuthenticationToken auth) {
        var userId = extractUserId(auth);
        var word = wordRepository.findById(request.getWordId()).orElseThrow(EntityNotFoundException::new);
        var userGroup = userGroupService.getUserGroupRepository().findByUserIdAndGroupId(userId, request.getGroupId()).orElseThrow(EntityNotFoundException::new);
        var group = userGroup.getGroup();

        if (userGroup.getIsOwner()) {
            var groupWord = GroupWord.builder()
                    .word(word)
                    .group(group)
                    .build();
            groupWordRepository.save(groupWord);
            return true;
        } else {
            throw new UnauthorizationException();
        }
    }

    public List<WordResponse> getWordsOfGroup(Integer groupId, JwtAuthenticationToken auth) {
        Integer userId = extractUserId(auth);
        Group group = userGroupService.getGroupService().findById(groupId);
        if (!group.getIsPrivate() || userGroupService.isOwner(userId, groupId)) {
            var words = new ArrayList<WordResponse>();
            groupWordRepository.findByGroupId(groupId).forEach(
                    (e) -> words.add(wordResponseMapper.toDTO(e.getWord()))
            );
            return words;
        } else {
            throw new UnauthorizationException();
        }
    }


}
