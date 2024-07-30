package com.phatpl.learnvocabulary.services;

import com.phatpl.learnvocabulary.dtos.request.DeleteWordRequest;
import com.phatpl.learnvocabulary.dtos.request.SaveWordRequest;
import com.phatpl.learnvocabulary.dtos.response.GroupResponse;
import com.phatpl.learnvocabulary.dtos.response.GroupWordResponse;
import com.phatpl.learnvocabulary.dtos.response.WordResponse;
import com.phatpl.learnvocabulary.exceptions.LimitedException;
import com.phatpl.learnvocabulary.exceptions.UnauthorizationException;
import com.phatpl.learnvocabulary.filters.BaseFilter;
import com.phatpl.learnvocabulary.mappers.GroupWordResponseMapper;
import com.phatpl.learnvocabulary.models.Group;
import com.phatpl.learnvocabulary.models.GroupWord;
import com.phatpl.learnvocabulary.models.UserGroup;
import com.phatpl.learnvocabulary.models.UserWord;
import com.phatpl.learnvocabulary.repositories.GroupWordRepository;
import com.phatpl.learnvocabulary.repositories.UserGroupRepository;
import com.phatpl.learnvocabulary.repositories.UserRepository;
import com.phatpl.learnvocabulary.repositories.UserWordRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public class GroupWordService extends BaseService<GroupWord, GroupWordResponse, BaseFilter, Integer> {
    GroupWordRepository groupWordRepository;
    GroupWordResponseMapper groupWordResponseMapper;
    WordService wordService;
    GroupService groupService;
    UserRepository userRepository;
    UserGroupRepository userGroupRepository;
    UserWordRepository userWordRepository;
    UserService userService;

    @Autowired
    public GroupWordService(GroupWordRepository groupWordRepository, GroupWordResponseMapper groupWordResponseMapper, WordService wordService, GroupService groupService, UserRepository userRepository, UserGroupRepository userGroupRepository, UserWordRepository userWordRepository, UserService userService) {
        super(groupWordResponseMapper, groupWordRepository);
        this.groupWordRepository = groupWordRepository;
        this.groupWordResponseMapper = groupWordResponseMapper;
        this.wordService = wordService;
        this.userRepository = userRepository;
        this.userGroupRepository = userGroupRepository;
        this.groupService = groupService;
        this.userWordRepository = userWordRepository;
        this.userService = userService;
    }

    public Boolean saveIntoGroup(SaveWordRequest request, Integer wordId) {
        var userId = userService.extractUserId();
        var user = userRepository.findById(userId).orElseThrow(UnauthorizationException::new);
        var word = wordService.findById(wordId);
        var userGroup = userGroupRepository.findByUserIdAndGroupId(userId, request.getGroupId()).orElseThrow(
                EntityNotFoundException::new
        );

        var group = userGroup.getGroup();
        if (userGroup.getIsOwner()) {
            if (group.getGroupWords().size() > 20) {
                throw new LimitedException("words");
            }
            var userWord = userWordRepository.findByUserIdAndWordId(userId, wordId);
            if (userWord.isEmpty()) {
                userWordRepository.save(new UserWord(1, user, word));
            }
            var groupWordOpt = groupWordRepository.findByGroupIdAndWordId(request.getGroupId(), wordId);
            if (groupWordOpt.isEmpty()) {
                persistEntity(new GroupWord(word, group));
            }
            return true;
        } else {
            throw new UnauthorizationException();
        }
    }

    public List<WordResponse> getWordsOfGroup(Integer groupId) {
        Integer userId = userService.extractUserId();
        Group group = groupService.findById(groupId);
        if (!group.getIsPrivate() || groupService.isOwner(userId, groupId)) {
            var words = new ArrayList<WordResponse>();
            groupWordRepository.findByGroupId(groupId).forEach(
                    (e) -> words.add(wordService.getWordResponseMapper().toDTO(e.getWord()))
            );
            return words;
        } else {
            throw new UnauthorizationException();
        }
    }

    public GroupResponse clone(Integer groupId) {
        var user = userRepository.findById(userService.extractUserId()).orElseThrow(UnauthorizationException::new);
        var oldGroup = groupService.findById(groupId);

        if (oldGroup.getIsPrivate() && !groupService.isOwner(user.getId(), groupId)) {
            throw new UnauthorizationException();
        }

        var words = oldGroup.getGroupWords();
        var newGroup = Group.builder()
                .name(oldGroup.getName())
                .isPrivate(true)
                .groupWords(new ArrayList<>())
                .build();
        var response = groupService.createDTO(newGroup);

        userGroupRepository.save(new UserGroup(true, user, newGroup));
        for (var word : words) {
            persistEntity(new GroupWord(
                    word.getWord(),
                    newGroup
            ));
            var userWord = userWordRepository.findByUserIdAndWordId(user.getId(), word.getWord().getId());
            if (userWord.isEmpty()) {
                userWordRepository.save(new UserWord(1, user, word.getWord()));
            }
        }

        return response;
    }

    public Boolean deleteFromGroup(DeleteWordRequest request, Integer wordId) {
        var userId = userService.extractUserId();
        var userGroup = userGroupRepository.findByUserIdAndGroupId(userId, request.getGroupId()).orElseThrow(
                UnauthorizationException::new
        );
        if (!userGroup.getIsOwner()) throw new UnauthorizationException();
        groupWordRepository.deleteByGroupIdAndWordId(request.getGroupId(), wordId);
        return true;
    }
}