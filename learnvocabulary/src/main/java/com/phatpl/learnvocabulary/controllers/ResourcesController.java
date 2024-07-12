package com.phatpl.learnvocabulary.controllers;

import com.phatpl.learnvocabulary.dtos.request.UpdateResourceRequest;
import com.phatpl.learnvocabulary.dtos.request.UploadResourceRequest;
import com.phatpl.learnvocabulary.dtos.response.ResourceResponse;
import com.phatpl.learnvocabulary.exceptions.UnauthorizationException;
import com.phatpl.learnvocabulary.filters.ResourcesFilter;
import com.phatpl.learnvocabulary.models.Resource;
import com.phatpl.learnvocabulary.services.ResourceService;
import com.phatpl.learnvocabulary.utils.BuildResponse;
import io.minio.errors.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.simpleframework.xml.core.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.management.RuntimeMBeanException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@RestController
@RequestMapping("/resources")
public class ResourcesController extends BaseController<Resource, ResourceResponse, ResourcesFilter, Integer> {

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
            return BuildResponse.ok(resourceService.save(request, auth));
        } catch (UnauthorizationException e) {
            return BuildResponse.unauthorized(e.getMessage());
        } catch (Exception e) {
            return BuildResponse.badRequest(e.getMessage());
        }
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable Integer id) {
        try {
            var response = resourceService.getResources(id);
            byte[] resource = response.readAllBytes();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
            return new ResponseEntity<>(resource, httpHeaders, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return BuildResponse.notFound(e.getMessage());
        } catch (RuntimeMBeanException | ServerException | ErrorResponseException | NoSuchAlgorithmException |
                 InvalidKeyException | InvalidResponseException | IOException | InsufficientDataException |
                 XmlParserException | InternalException e) {
            return BuildResponse.badRequest(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable Integer id) {
        try {
            resourceService.deleteById(id);
            return BuildResponse.ok("deleted resources id = " + id);
        } catch (Exception e) {
            return BuildResponse.badRequest(e.getMessage());
        }
    }

    @Override
    @GetMapping
    public ResponseEntity findAll(@RequestBody ResourcesFilter request) {
        try {
            return BuildResponse.ok(
                    resourceService.search(request)
            );
        } catch (RuntimeException e) {
            return BuildResponse.badRequest(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping("/delete-all")
    public ResponseEntity deleteAll() {
        resourceService.deleteAll();
        return BuildResponse.ok("deleted");
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") Integer id, @RequestBody UpdateResourceRequest request) {
        try {
            return BuildResponse.ok(resourceService.update(request, id));
        } catch (EntityNotFoundException e) {
            return BuildResponse.notFound(e.getMessage());
        } catch (RuntimeException | ServerException | InsufficientDataException | ErrorResponseException | IOException |
                 NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException | XmlParserException |
                 InternalException e) {
            return BuildResponse.badRequest(e.getMessage());
        }
    }
}
