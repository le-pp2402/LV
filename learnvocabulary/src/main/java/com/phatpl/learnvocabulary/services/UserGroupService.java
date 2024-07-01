package com.phatpl.learnvocabulary.services;

import com.phatpl.learnvocabulary.dtos.request.CreateGroupRequest;
import com.phatpl.learnvocabulary.dtos.response.GroupResponse;
import com.phatpl.learnvocabulary.dtos.response.UserGroupResponse;
import com.phatpl.learnvocabulary.exceptions.BadRequestException;
import com.phatpl.learnvocabulary.exceptions.LimitedException;
import com.phatpl.learnvocabulary.exceptions.UnauthorizationException;
import com.phatpl.learnvocabulary.filters.BaseFilter;
import com.phatpl.learnvocabulary.mappers.UserGroupResponseMapper;
import com.phatpl.learnvocabulary.models.Group;
import com.phatpl.learnvocabulary.models.UserGroup;
import com.phatpl.learnvocabulary.repositories.GroupWordRepository;
import com.phatpl.learnvocabulary.repositories.UserGroupRepository;
import com.phatpl.learnvocabulary.repositories.UserRepository;
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
    UserRepository userRepository;
    GroupWordRepository groupWordRepository;

    @Autowired
    public UserGroupService(UserGroupResponseMapper userGroupResponseMapper, UserGroupRepository userGroupRepository, GroupService groupService, UserRepository userRepository, GroupWordRepository groupWordRepository) {
        super(userGroupResponseMapper, userGroupRepository);
        this.userGroupResponseMapper = userGroupResponseMapper;
        this.userGroupRepository = userGroupRepository;
        this.groupService = groupService;
        this.userRepository = userRepository;
        this.groupWordRepository = groupWordRepository;
    }

    public Boolean isOwner(Integer userId, Integer groupId) {
        var userGroup = userGroupRepository.findByUserIdAndGroupId(userId, groupId);
        return userGroup.isPresent() && userGroup.get().getIsOwner();
    }

    public GroupResponse create(CreateGroupRequest request, JwtAuthenticationToken auth) {
        Integer userId = extractUserId(auth);
        if (userGroupRepository.numberOfGroups(userId) > 20) {
            throw new LimitedException("groups");
        }
        var group = Group.builder().name(request.getName()).isPrivate(true).build();
        var user = userRepository.findById(userId).get();
        UserGroup userGroup = new UserGroup(true, user, group);
        var groupDTO = groupService.createDTO(group);
        this.persistEntity(userGroup);
        return groupDTO;
    }

    public void delete(Integer groupId, JwtAuthenticationToken auth) {
        var userId = extractUserId(auth);
        if (isOwner(userId, groupId)) {
            userGroupRepository.deleteByGroupId(groupId);
            groupService.deleteById(userId);
        } else {
            userGroupRepository.deleteByUserIdAndGroupId(userId, groupId);
        }
    }

    public UserGroupResponse follow(Integer groupId, JwtAuthenticationToken auth) {
        var userId = extractUserId(auth);
        try {
            var userGroup = userGroupRepository.findByUserIdAndGroupId(userId, groupId).orElseThrow(() -> new BadRequestException("Thong tin khong hop le"));
            return userGroupResponseMapper.toDTO(userGroup);
        } catch (EntityNotFoundException e) {
            var group = groupService.findById(groupId);
            if (group.getIsPrivate()) throw new UnauthorizationException();
            return this.createDTO(
                    new UserGroup(
                            false, userRepository.findById(userId).get(), group
                    ));
        }
    }
}
