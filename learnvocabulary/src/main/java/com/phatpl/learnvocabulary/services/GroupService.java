package com.phatpl.learnvocabulary.services;

import com.phatpl.learnvocabulary.dtos.request.UpdateGroupRequest;
import com.phatpl.learnvocabulary.dtos.response.GroupResponse;
import com.phatpl.learnvocabulary.exceptions.UnauthorizationException;
import com.phatpl.learnvocabulary.filters.GroupFilter;
import com.phatpl.learnvocabulary.mappers.GroupResponseMapper;
import com.phatpl.learnvocabulary.models.Group;
import com.phatpl.learnvocabulary.models.User;
import com.phatpl.learnvocabulary.repositories.jpa.GroupRepository;
import com.phatpl.learnvocabulary.repositories.jpa.UserGroupRepository;
import com.phatpl.learnvocabulary.repositories.jpa.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public class GroupService extends BaseService<Group, GroupResponse, GroupFilter, Integer> {
    GroupResponseMapper groupResponseMapper;
    GroupRepository groupRepository;
    UserRepository userRepository;
    UserGroupRepository userGroupRepository;
    UserService userService;

    @Autowired
    public GroupService(GroupRepository groupRepository, GroupResponseMapper groupResponseMapper, UserRepository userRepository, UserGroupRepository userGroupRepository, UserService userService) {
        super(groupResponseMapper, groupRepository);
        this.groupRepository = groupRepository;
        this.groupResponseMapper = groupResponseMapper;
        this.userRepository = userRepository;
        this.userGroupRepository = userGroupRepository;
        this.userService = userService;
    }

    public Boolean isOwner(Integer userId, Integer groupId) {
        var userGroup = userGroupRepository.findByUserIdAndGroupId(userId, groupId);
        return userGroup.isPresent() && userGroup.get().getIsOwner();
    }

    public List<GroupResponse> findGroupByUser() {
        var userId = userService.extractUserId();
        User user = userRepository.findById(userId).orElseThrow(
                EntityNotFoundException::new
        );
        var groups = groupRepository.findByUserId(user.getId());
        return groupResponseMapper.toListDTO(groups);
    }

    public GroupResponse updateGroupInfo(Integer groupId, UpdateGroupRequest updateGroupRequest) {
        var userId = userService.extractUserId();
        if (!isOwner(userId, groupId)) throw new UnauthorizationException();
        var group = findById(groupId);
        group.setName(updateGroupRequest.getName());
        group.setIsPrivate(updateGroupRequest.getIsPrivate());
        if (updateGroupRequest.getIsPrivate()) {
            userGroupRepository.deleteByGroupIdAndUserIdNot(groupId, userId);
        }
        return createDTO(group);
    }
}