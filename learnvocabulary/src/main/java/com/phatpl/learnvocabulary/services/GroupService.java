package com.phatpl.learnvocabulary.services;

import com.phatpl.learnvocabulary.dtos.response.GroupResponse;
import com.phatpl.learnvocabulary.filters.GroupFilter;
import com.phatpl.learnvocabulary.mappers.CreateGroupRequestMapper;
import com.phatpl.learnvocabulary.mappers.GroupResponseMapper;
import com.phatpl.learnvocabulary.mappers.UserGroupResponseMapper;
import com.phatpl.learnvocabulary.mappers.WordResponseMapper;
import com.phatpl.learnvocabulary.models.Group;
import com.phatpl.learnvocabulary.models.User;
import com.phatpl.learnvocabulary.repositories.GroupRepository;
import com.phatpl.learnvocabulary.repositories.UserGroupRepository;
import com.phatpl.learnvocabulary.repositories.UserRepository;
import com.phatpl.learnvocabulary.repositories.WordRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GroupService extends BaseService<Group, GroupResponse, GroupFilter, Integer> {
    UserRepository userRepository;
    GroupResponseMapper groupResponseMapper;
    GroupRepository groupRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository, UserGroupRepository userGroupRepository, CreateGroupRequestMapper createGroupRequestMapper, UserRepository userRepository, GroupResponseMapper groupResponseMapper, UserGroupResponseMapper userGroupResponseMapper, WordRepository wordRepository, WordResponseMapper wordResponseMapper) {
        super(groupResponseMapper, groupRepository);
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.groupResponseMapper = groupResponseMapper;
    }
    
    public List<GroupResponse> findGroupByUser(JwtAuthenticationToken jwtAuth) {
        var userId = extractUserId(jwtAuth);
        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("not found user with id = " + userId)
        );
        var groups = groupRepository.findByUserId(user.getId());
        return groupResponseMapper.toListDTO(groups);
    }

    public Group findById(Integer groupId) {
        return groupRepository.findById(groupId).orElseThrow(EntityNotFoundException::new);
    }

}