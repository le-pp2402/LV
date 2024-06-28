package com.phatpl.learnvocabulary.services;

import com.phatpl.learnvocabulary.dtos.request.CreateGroupRequest;
import com.phatpl.learnvocabulary.dtos.response.UserGroupResponse;
import com.phatpl.learnvocabulary.exceptions.LimitedException;
import com.phatpl.learnvocabulary.exceptions.UnauthorizationException;
import com.phatpl.learnvocabulary.filters.BaseFilter;
import com.phatpl.learnvocabulary.mappers.UserGroupResponseMapper;
import com.phatpl.learnvocabulary.models.Group;
import com.phatpl.learnvocabulary.models.UserGroup;
import com.phatpl.learnvocabulary.repositories.GroupWordRepository;
import com.phatpl.learnvocabulary.repositories.UserGroupRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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
    UserService userService;
    GroupWordRepository groupWordRepository;

    @Autowired
    public UserGroupService(UserGroupResponseMapper userGroupResponseMapper, UserGroupRepository userGroupRepository, GroupService groupService, UserService userService, GroupWordRepository groupWordRepository) {
        super(userGroupResponseMapper, userGroupRepository);
        this.userGroupResponseMapper = userGroupResponseMapper;
        this.userGroupRepository = userGroupRepository;
        this.groupService = groupService;
        this.userService = userService;
        this.groupWordRepository = groupWordRepository;
    }

    public UserGroup findByUserIdAndGroupId(Integer userId, Integer groupId) {
        return userGroupRepository.findByUserIdAndGroupId(userId, groupId).orElseThrow(EntityNotFoundException::new);
    }

    public Boolean isOwner(Integer userId, Integer groupId) {
        try {
            findByUserIdAndGroupId(userId, groupId);
            return true;
        } catch (EntityNotFoundException e) {
            return false;
        }
    }

    public UserGroupResponse create(CreateGroupRequest request, JwtAuthenticationToken auth) {
        Integer userId = extractUserId(auth);
        if (userGroupRepository.numberOfGroups(userId) > 20) {
            throw new LimitedException("groups");
        }
        var group = Group.builder().name(request.getName()).isPrivate(true).build();
        var user = userService.findById(userId);
        UserGroup userGroup = new UserGroup(true, user, group);
        groupService.persistEntity(group);
        return createDTO(userGroup);
    }

    public void delete(Integer groupId, JwtAuthenticationToken auth) {
        var userId = extractUserId(auth);
        if (isOwner(userId, groupId)) {
            userGroupRepository.deleteByGroupId(groupId);
            groupService.deleteById(userId);
        } else {
            userGroupRepository.deleteById(findByUserIdAndGroupId(userId, groupId).getId());
        }
    }

    public UserGroupResponse follow(Integer groupId, JwtAuthenticationToken auth) {
        var userId = extractUserId(auth);
        try {
            var userGroup = findByUserIdAndGroupId(userId, groupId);
            return userGroupResponseMapper.toDTO(userGroup);
        } catch (EntityNotFoundException e) {
            var group = groupService.findById(groupId);
            if (group.getIsPrivate()) throw new UnauthorizationException();
            return this.createDTO(
                    new UserGroup(
                            false, userService.findById(userId), group
                    ));
        }
    }
}
