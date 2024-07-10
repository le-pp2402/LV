package com.phatpl.learnvocabulary.controllers;

import com.phatpl.learnvocabulary.dtos.request.UploadResourceRequest;
import com.phatpl.learnvocabulary.dtos.response.ResourceResponse;
import com.phatpl.learnvocabulary.exceptions.UnauthorizationException;
import com.phatpl.learnvocabulary.filters.BaseFilter;
import com.phatpl.learnvocabulary.models.Resource;
import com.phatpl.learnvocabulary.services.ResourceService;
import com.phatpl.learnvocabulary.utils.BuildResponse;
import lombok.extern.slf4j.Slf4j;
import org.simpleframework.xml.core.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/resources")
public class ResourcesController extends BaseController<Resource, ResourceResponse, BaseFilter, Integer> {

    @Autowired
    private final ResourceService resourceService;

    @Autowired
    public ResourcesController(ResourceService resourceService) {
        super(resourceService);
        this.resourceService = resourceService;
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PostMapping("/upload")
    public ResponseEntity upload(@ModelAttribute @Validate UploadResourceRequest request) {
        try {
            JwtAuthenticationToken auth = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            resourceService.save(request, auth);
            return BuildResponse.ok("uploaded");
        } catch (UnauthorizationException e) {
            return BuildResponse.unauthorized(e.getMessage());
        } catch (Exception e) {
            return BuildResponse.badRequest(e.getMessage());
        }
    }

    @GetMapping("/q")
    public ResponseEntity findAll(@RequestBody Map<String, Object> keyword) {
        try {
            return BuildResponse.ok(
                    resourceService.search((String) keyword.get("keyword"))
            );
        } catch (RuntimeException e) {
            return BuildResponse.badRequest(e.getMessage());
        }

    }
}
