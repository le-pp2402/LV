package com.phatpl.learnvocabulary.services;

import com.phatpl.learnvocabulary.dtos.BaseDTO;
import com.phatpl.learnvocabulary.dtos.Response;
import com.phatpl.learnvocabulary.dtos.response.GroupResponse;
import com.phatpl.learnvocabulary.filters.GroupFilter;
import com.phatpl.learnvocabulary.mappers.BaseMapper;
import com.phatpl.learnvocabulary.mappers.GroupResponseMapper;
import com.phatpl.learnvocabulary.mappers.UserGroupResponseMapper;
import com.phatpl.learnvocabulary.models.Group;
import com.phatpl.learnvocabulary.repositories.BaseRepository;
import com.phatpl.learnvocabulary.repositories.GroupRepository;
import com.phatpl.learnvocabulary.repositories.UserGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class GroupService extends BaseService<Group, GroupResponse, GroupFilter, Integer> {
    private final GroupRepository groupRepository;
    private final GroupResponseMapper groupResponseMapper;
    private final UserGroupRepository userGroupRepository;
    private final UserGroupResponseMapper userGroupResponseMapper;
    private final JWTService jwtService;
    @Autowired
    public GroupService(GroupResponseMapper groupResponseMapper, GroupRepository groupRepository, UserGroupRepository userGroupRepository, UserGroupResponseMapper userGroupResponseMapper, JWTService jwtService) {
        super(groupResponseMapper, groupRepository);
        this.groupRepository = groupRepository;
        this.groupResponseMapper = groupResponseMapper;
        this.userGroupRepository = userGroupRepository;
        this.userGroupResponseMapper = userGroupResponseMapper;
        this.jwtService = jwtService;
    }


    public Response getGroupWithFilter(String token, GroupFilter groupFilter) {
        try {
            var object = (Map<String, Object>) jwtService.verifyToken(token).getBody().get("data");
            var groups = userGroupRepository.findByUser((Integer)object.get("id"));
            return Response.builder().code(HttpStatus.OK.value()).data(userGroupResponseMapper.toListDTO(groups)).message("Success").build();
        } catch (Exception e) {
            return Response.builder().code(HttpStatus.UNAUTHORIZED.value()).data("").message(e.getMessage()).build();
        }
    }
}