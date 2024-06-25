package com.phatpl.learnvocabulary.controllers;


import com.phatpl.learnvocabulary.dtos.request.CreateGroupRequest;
import com.phatpl.learnvocabulary.dtos.request.UpdateGroupRequest;
import com.phatpl.learnvocabulary.dtos.response.GroupResponse;
import com.phatpl.learnvocabulary.exceptions.UnauthorizationException;
import com.phatpl.learnvocabulary.filters.GroupFilter;
import com.phatpl.learnvocabulary.models.Group;
import com.phatpl.learnvocabulary.services.GroupService;
import com.phatpl.learnvocabulary.utils.BuildResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/groups")
public class GroupController extends BaseController<Group, GroupResponse, GroupFilter, Integer> {

    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        super(groupService);
        this.groupService = groupService;
    }

    @PostMapping("/me")
    public ResponseEntity createGroup(@RequestBody @Valid CreateGroupRequest createGroupRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            var error = bindingResult.getFieldErrors().get(0);
            return BuildResponse.badRequest(error.getDefaultMessage());
        }
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            return BuildResponse.created(groupService.create(createGroupRequest, auth));
        } catch (RuntimeException e) {
            return BuildResponse.badRequest(e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity findGroupByUser() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            return BuildResponse.ok(groupService.findGroupByUser(auth.getName()));
        } catch (RuntimeException e) {
            return BuildResponse.badRequest(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity updateGroupInfo(@PathVariable("id") Integer id, @RequestBody UpdateGroupRequest updateGroupRequest) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            return BuildResponse.ok(groupService.updateGroupInfo(id, updateGroupRequest, auth.getName()));
        } catch (UnauthorizationException e) {
            return BuildResponse.unauthorized(e.getMessage());
        } catch (RuntimeException e) {
            return BuildResponse.badRequest(e.getMessage());
        }
    }

    @PostMapping("/{id}/follow")
    public ResponseEntity followGroup(@NotNull @PathVariable("id") Integer id) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            return BuildResponse.ok(groupService.follow(auth.getName(), id));
        } catch (UnauthorizationException e) {
            return BuildResponse.unauthorized(e.getMessage());
        } catch (RuntimeException e) {
            return BuildResponse.badRequest(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Integer id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            groupService.delete(id, auth);
            return BuildResponse.ok("deleted group with id = " + id);
        } catch (Exception e) {
            return BuildResponse.forbidden(e.getMessage());
        }
    }
}