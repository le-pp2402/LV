package com.phatpl.learnvocabulary.controllers;

import com.phatpl.learnvocabulary.dtos.request.CreateGroupRequest;
import com.phatpl.learnvocabulary.dtos.request.UpdateGroupRequest;
import com.phatpl.learnvocabulary.dtos.response.GroupResponse;
import com.phatpl.learnvocabulary.exceptions.UnauthorizationException;
import com.phatpl.learnvocabulary.filters.GroupFilter;
import com.phatpl.learnvocabulary.models.Group;
import com.phatpl.learnvocabulary.services.GroupService;
import com.phatpl.learnvocabulary.services.UserGroupService;
import com.phatpl.learnvocabulary.services.WordService;
import com.phatpl.learnvocabulary.utils.BuildResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/groups")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GroupController extends BaseController<Group, GroupResponse, GroupFilter, Integer> {

    GroupService groupService;
    UserGroupService userGroupService;
    WordService wordService;

    @Autowired
    public GroupController(GroupService groupService, UserGroupService userGroupService, WordService wordService) {
        super(groupService);
        this.groupService = groupService;
        this.userGroupService = userGroupService;
        this.wordService = wordService;
    }

    @PostMapping("/me")
    public ResponseEntity createGroup(@RequestBody @Valid CreateGroupRequest createGroupRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            var error = bindingResult.getFieldErrors().get(0);
            return BuildResponse.badRequest(error.getDefaultMessage());
        }
        try {
            JwtAuthenticationToken auth = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            return BuildResponse.created(userGroupService.create(createGroupRequest, auth));
        } catch (RuntimeException e) {
            return BuildResponse.badRequest(e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity findGroupByUser() {
        try {
            JwtAuthenticationToken auth = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            return BuildResponse.ok(groupService.findGroupByUser(auth));
        } catch (RuntimeException e) {
            return BuildResponse.badRequest(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity updateGroupInfo(@PathVariable("id") Integer groupId, @RequestBody UpdateGroupRequest updateGroupRequest) {
        try {
            JwtAuthenticationToken auth = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            return BuildResponse.ok(userGroupService.updateGroupInfo(groupId, updateGroupRequest, auth));
        } catch (UnauthorizationException e) {
            return BuildResponse.unauthorized(e.getMessage());
        } catch (RuntimeException e) {
            return BuildResponse.badRequest(e.getMessage());
        }
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable("id") Integer groupId) {
        try {
            JwtAuthenticationToken auth = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            return BuildResponse.ok(wordService.getWordsOfGroup(groupId, auth));
        } catch (RuntimeException e) {
            return BuildResponse.badRequest(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Integer groupId) {
        try {
            JwtAuthenticationToken auth = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            userGroupService.delete(groupId, auth);
            return BuildResponse.ok("deleted group id = " + groupId);
        } catch (Exception e) {
            return BuildResponse.forbidden(e.getMessage());
        }
    }

    @PostMapping("/{id}/follow")
    public ResponseEntity followGroup(@NotNull @PathVariable("id") Integer groupId) {
        try {
            JwtAuthenticationToken auth = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            return BuildResponse.ok(userGroupService.follow(groupId, auth));
        } catch (UnauthorizationException e) {
            return BuildResponse.unauthorized(e.getMessage());
        } catch (RuntimeException e) {
            return BuildResponse.badRequest(e.getMessage());
        }
    }
}