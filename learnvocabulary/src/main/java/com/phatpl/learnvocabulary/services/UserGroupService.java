package com.phatpl.learnvocabulary.services;

import com.phatpl.learnvocabulary.dtos.request.CreateGroupRequest;
import com.phatpl.learnvocabulary.dtos.response.GroupResponse;
import com.phatpl.learnvocabulary.dtos.response.UserGroupResponse;
import com.phatpl.learnvocabulary.exceptions.LimitedException;
import com.phatpl.learnvocabulary.exceptions.UnauthorizationException;
import com.phatpl.learnvocabulary.filters.BaseFilter;
import com.phatpl.learnvocabulary.mappers.UserGroupResponseMapper;
import com.phatpl.learnvocabulary.models.Group;
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

@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public class UserGroupService extends BaseService<UserGroup, UserGroupResponse, BaseFilter, Integer> {
    UserGroupResponseMapper userGroupResponseMapper;
    UserGroupRepository userGroupRepository;
    GroupService groupService;
    UserRepository userRepository;
    GroupWordRepository groupWordRepository;
    UserWordRepository userWordRepository;
    UserService userService;

    @Autowired
    public UserGroupService(UserGroupResponseMapper userGroupResponseMapper, UserGroupRepository userGroupRepository, GroupService groupService, UserRepository userRepository, GroupWordRepository groupWordRepository, UserWordRepository userWordRepository, UserService userService) {
        super(userGroupResponseMapper, userGroupRepository);
        this.userGroupResponseMapper = userGroupResponseMapper;
        this.userGroupRepository = userGroupRepository;
        this.groupService = groupService;
        this.userRepository = userRepository;
        this.groupWordRepository = groupWordRepository;
        this.userWordRepository = userWordRepository;
        this.userService = userService;
    }

    public Boolean isOwner(Integer userId, Integer groupId) {
        var userGroup = userGroupRepository.findByUserIdAndGroupId(userId, groupId);
        return userGroup.isPresent() && userGroup.get().getIsOwner();
    }

    public GroupResponse create(CreateGroupRequest request) {
        Integer userId = userService.extractUserId();
        if (userGroupRepository.numberOfGroups(userId) > 20) {
            throw new LimitedException("groups");
        }
        var group = Group.builder().name(request.getName()).isPrivate(true).build();
        var user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        UserGroup userGroup = new UserGroup(true, user, group);
        var groupDTO = groupService.createDTO(group);
        this.persistEntity(userGroup);
        return groupDTO;
    }

    public void delete(Integer groupId) {
        var userId = userService.extractUserId();
        if (isOwner(userId, groupId)) {
            userGroupRepository.deleteByGroupId(groupId);
            groupService.deleteById(userId);
        } else {
            userGroupRepository.deleteByUserIdAndGroupId(userId, groupId);
        }
    }

    public UserGroupResponse follow(Integer groupId) {
        var userId = userService.extractUserId();
        var user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        var userGroup = userGroupRepository.findByUserIdAndGroupId(userId, groupId);
        if (userGroup.isPresent())
            return userGroupResponseMapper.toDTO(userGroup.get());
        else {
            var group = groupService.findById(groupId);
            if (group.getIsPrivate()) throw new UnauthorizationException();
            var words = group.getGroupWords();
            for (var word : words) {
                var userWord = userWordRepository.findByUserIdAndWordId(userId, word.getWord().getId());
                if (userWord.isEmpty()) {
                    userWordRepository.save(new UserWord(1, user, word.getWord()));
                }
            }
            return this.createDTO(
                    new UserGroup(
                            false, user, group
                    ));
        }
    }
}