package com.phatpl.learnvocabulary.controllers;


import com.phatpl.learnvocabulary.dtos.Response;
import com.phatpl.learnvocabulary.dtos.request.CreateGroupRequest;
import com.phatpl.learnvocabulary.dtos.request.UpdateGroupRequest;
import com.phatpl.learnvocabulary.dtos.response.GroupResponse;
import com.phatpl.learnvocabulary.filters.GroupFilter;
import com.phatpl.learnvocabulary.mappers.GroupRequestMapper;
import com.phatpl.learnvocabulary.models.Group;
import com.phatpl.learnvocabulary.models.User;
import com.phatpl.learnvocabulary.models.UserGroup;
import com.phatpl.learnvocabulary.repositories.GroupRepository;
import com.phatpl.learnvocabulary.repositories.UserGroupRepository;
import com.phatpl.learnvocabulary.repositories.UserRepository;
import com.phatpl.learnvocabulary.services.GroupService;
import com.phatpl.learnvocabulary.services.JWTService;
import com.phatpl.learnvocabulary.utils.Logger;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/groups")
public class GroupController extends BaseController<Group, GroupResponse, GroupFilter, Integer> {

    private final GroupRepository groupRepository;
    private final GroupService groupService;
    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;
    private final JWTService jwtService;
    @Autowired
    public GroupController(GroupRepository groupRepository, GroupService groupService, UserRepository userRepository, UserGroupRepository userGroupRepository, JWTService jwtService) {
        super(groupService);
        this.groupRepository = groupRepository;
        this.groupService = groupService;
        this.userRepository = userRepository;
        this.userGroupRepository = userGroupRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("/me")
    public ResponseEntity createGroup(HttpServletRequest request, @RequestBody @Valid CreateGroupRequest createGroupRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Logger.error("loi");
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE.value())
                    .body(bindingResult.getFieldErrors().get(0).getDefaultMessage());
        }
        String token = request.getHeader("Authorization").substring(7);
        try {
            var group = GroupRequestMapper.instance.toEntity(createGroupRequest);

            var body = jwtService.verifyToken(token).getBody();
            Map<String, Object> obj = (Map<String, Object>) body.get("data");
            User user = userRepository.findById((Integer)obj.get("id")).get();
            UserGroup userGroup = UserGroup.builder().group(group).user(user).build();
            userGroup.setIsOwner(true);

            Integer groupId = groupRepository.save(group).getId();
            userGroupRepository.save(userGroup);
            return Response.ok(groupRepository.findById(groupId));
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity updateGroupInfo(@NotNull @PathVariable("id") Integer id, @RequestBody UpdateGroupRequest updateGroupRequest) {
        // check authrization
        Logger.log(updateGroupRequest.toString());
        return Response.ok(groupService.updateGroupInfo(id, updateGroupRequest));
    }
    @GetMapping("/me")
    public ResponseEntity getGroupOfUser(HttpServletRequest request, GroupFilter groupFilter) {
        // check authorization
        String token = request.getHeader("Authorization").substring(7);
        var body = jwtService.verifyToken(token).getBody();
        Map<String, Object> obj = (Map<String, Object>) body.get("data");
        User user = userRepository.findById((Integer)obj.get("id")).get();
        return Response.ok(user.getUserGroups());
    }
}