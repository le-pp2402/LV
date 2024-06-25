package com.phatpl.learnvocabulary.services;

import com.phatpl.learnvocabulary.dtos.request.CreateGroupRequest;
import com.phatpl.learnvocabulary.dtos.request.UpdateGroupRequest;
import com.phatpl.learnvocabulary.dtos.response.GroupResponse;
import com.phatpl.learnvocabulary.dtos.response.UserGroupResponse;
import com.phatpl.learnvocabulary.exceptions.LimitedException;
import com.phatpl.learnvocabulary.exceptions.UnauthorizationException;
import com.phatpl.learnvocabulary.filters.GroupFilter;
import com.phatpl.learnvocabulary.mappers.CreateGroupRequestMapper;
import com.phatpl.learnvocabulary.mappers.GroupResponseMapper;
import com.phatpl.learnvocabulary.mappers.UserGroupResponseMapper;
import com.phatpl.learnvocabulary.models.Group;
import com.phatpl.learnvocabulary.models.User;
import com.phatpl.learnvocabulary.models.UserGroup;
import com.phatpl.learnvocabulary.repositories.GroupRepository;
import com.phatpl.learnvocabulary.repositories.UserGroupRepository;
import com.phatpl.learnvocabulary.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GroupService extends BaseService<Group, GroupResponse, GroupFilter, Integer> {
    private final GroupRepository groupRepository;
    private final UserGroupRepository userGroupRepository;
    private final CreateGroupRequestMapper createGroupRequestMapper;
    private final UserRepository userRepository;
    private final GroupResponseMapper groupResponseMapper;
    private final UserGroupResponseMapper userGroupResponseMapper;

    @Autowired
    public GroupService(GroupRepository groupRepository, UserGroupRepository userGroupRepository, CreateGroupRequestMapper createGroupRequestMapper, UserRepository userRepository, GroupResponseMapper groupResponseMapper, UserGroupResponseMapper userGroupResponseMapper) {
        super(groupResponseMapper, groupRepository);
        this.groupRepository = groupRepository;
        this.userGroupRepository = userGroupRepository;
        this.createGroupRequestMapper = createGroupRequestMapper;
        this.userRepository = userRepository;
        this.groupResponseMapper = groupResponseMapper;
        this.userGroupResponseMapper = userGroupResponseMapper;
    }


    public List<GroupResponse> findGroupByUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException("not found " + username)
        );
        var groups = groupRepository.findByUserId(user.getId());
        return groupResponseMapper.toListDTO(groups);
    }

    public GroupResponse updateGroupInfo(Integer id, UpdateGroupRequest updateGroupRequest, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UnauthorizationException("Accept deny")
        );
        Group group = groupRepository.findById(id).orElseThrow(
                () -> new UnauthorizationException("Accept deny")
        );
        UserGroup userGroup = userGroupRepository.findByUserIdAndGroupId(user.getId(), group.getId()).orElseThrow(
                () -> new UnauthorizationException("Accept deny")
        );

        if (!userGroup.getIsOwner()) {
            throw new UnauthorizationException("Accept deny");
        }

        group.setName(updateGroupRequest.getName());
        group.setIsPrivate(updateGroupRequest.getIsPrivate());

        return groupResponseMapper.toDTO(groupRepository.save(group));
    }

    public UserGroupResponse create(CreateGroupRequest request, Authentication auth) {
        var group = createGroupRequestMapper.toEntity(request);
        User user = userRepository.findByUsername(auth.getName()).get();
        if (userGroupRepository.numberOfGroups(user.getId()) > 20) {
            throw new LimitedException("groups");
        }

        UserGroup userGroup = UserGroup.builder().group(group).user(user).build();
        userGroup.setIsOwner(true);

        groupRepository.save(group);
        Integer idGroup = userGroupRepository.save(userGroup).getId();
        return userGroupResponseMapper.toDTO(userGroupRepository.findById(idGroup).get());
    }

    public UserGroupResponse followGroup(String username, Integer id) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException(username + " not found")
        );
        Group group = groupRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("group with id = " + id + "not found")
        );
        var userGroup = userGroupRepository.findByUserIdAndGroupId(user.getId(), group.getId());
        if (userGroup.isPresent()) {
            return userGroupResponseMapper.toDTO(userGroup.get());
        }
        if (group.getIsPrivate()) {
            throw new UnauthorizationException("access deny");
        }

        var savedGroup = userGroupRepository.save(
                UserGroup.builder()
                        .group(group)
                        .user(user)
                        .isOwner(false)
                        .build()
        );

        return userGroupResponseMapper.toDTO(savedGroup);
    }

    public void delete(Integer id, Authentication auth) {
        User user = userRepository.findByUsername(auth.getName()).orElseThrow(
                () -> new EntityNotFoundException()
        );
        Group group = groupRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException()
        );
        UserGroup userGroup = userGroupRepository.findByUserIdAndGroupId(user.getId(), group.getId()).orElseThrow(
                () -> new EntityNotFoundException()
        );

        if (userGroup.getIsOwner()) {
            userGroupRepository.deleteByGroupId(group.getId());
            groupRepository.deleteById(group.getId());
        } else {
            userGroupRepository.deleteByUserIdAndGroupId(user.getId(), group.getId());
        }
    }
}