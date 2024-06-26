package com.phatpl.learnvocabulary.services;

import com.phatpl.learnvocabulary.dtos.request.CreateGroupRequest;
import com.phatpl.learnvocabulary.dtos.request.UpdateGroupRequest;
import com.phatpl.learnvocabulary.dtos.response.GroupResponse;
import com.phatpl.learnvocabulary.dtos.response.UserGroupResponse;
import com.phatpl.learnvocabulary.exceptions.LimitedException;
import com.phatpl.learnvocabulary.exceptions.UnauthorizationException;
import com.phatpl.learnvocabulary.filters.BaseFilter;
import com.phatpl.learnvocabulary.mappers.UserGroupResponseMapper;
import com.phatpl.learnvocabulary.models.UserGroup;
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

    @Autowired
    public UserGroupService(UserGroupResponseMapper userGroupResponseMapper, UserGroupRepository userGroupRepository, GroupService groupService, UserRepository userRepository) {
        super(userGroupResponseMapper, userGroupRepository);
        this.userGroupResponseMapper = userGroupResponseMapper;
        this.userGroupRepository = userGroupRepository;
        this.groupService = groupService;
        this.userRepository = userRepository;
    }

    public Boolean isOwner(Integer userId, Integer groupId) {
        var userGroup = userGroupRepository.findByUserIdAndGroupId(userId, groupId);
        return userGroup.isPresent();
    }

    public UserGroupResponse create(CreateGroupRequest request, JwtAuthenticationToken auth) {
        Integer userId = extractUserId(auth);
        if (userGroupRepository.numberOfGroups(userId) > 20) {
            throw new LimitedException("groups");
        }
        var group = groupService.findById(request.getId());
        var user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        UserGroup userGroup = UserGroup.builder().group(group).user(user).isOwner(true).build();
        groupService.save(group);
        return this.save(userGroup);
    }

    public GroupResponse updateGroupInfo(Integer groupId, UpdateGroupRequest updateGroupRequest, JwtAuthenticationToken jwtAuth) {
        var userId = extractUserId(jwtAuth);
        if (!isOwner(userId, groupId)) throw new UnauthorizationException();
        var group = groupService.findById(groupId);
        group.setName(updateGroupRequest.getName());
        group.setIsPrivate(updateGroupRequest.getIsPrivate());
        return groupService.save(group);
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
        var userGroup = userGroupRepository.findByUserIdAndGroupId(userId, groupId);
        if (userGroup.isPresent()) {
            return userGroupResponseMapper.toDTO(userGroup.get());
        } else {
            var group = userGroup.get().getGroup();
            if (group.getIsPrivate()) throw new UnauthorizationException();

            return this.save(
                    UserGroup.builder()
                            .group(userGroup.get().getGroup())
                            .user(userGroup.get().getUser())
                            .isOwner(false)
                            .build()
            );
        }
    }


}
