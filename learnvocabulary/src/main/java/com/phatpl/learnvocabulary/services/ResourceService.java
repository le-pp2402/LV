package com.phatpl.learnvocabulary.services;

import com.meilisearch.sdk.Index;
import com.phatpl.learnvocabulary.dtos.request.UpdateResourceRequest;
import com.phatpl.learnvocabulary.dtos.request.UploadResourceRequest;
import com.phatpl.learnvocabulary.dtos.response.ResourceResponse;
import com.phatpl.learnvocabulary.exceptions.BadRequestException;
import com.phatpl.learnvocabulary.exceptions.UnauthorizationException;
import com.phatpl.learnvocabulary.filters.ResourcesFilter;
import com.phatpl.learnvocabulary.mappers.ResourceResponseMapper;
import com.phatpl.learnvocabulary.models.Resource;
import com.phatpl.learnvocabulary.repositories.ResourceRepository;
import com.phatpl.learnvocabulary.repositories.UserRepository;
import com.phatpl.learnvocabulary.utils.DefineDatatype.Pair;
import io.minio.errors.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Transactional
public class ResourceService extends BaseService<Resource, ResourceResponse, ResourcesFilter, Integer> {

    ResourceRepository resourceRepository;
    ResourceResponseMapper resourceResponseMapper;
    MinIOService minIOService;
    UserRepository userRepository;
    MeliSearchService meliSearchService;

    @Autowired
    public ResourceService(ResourceRepository resourceRepository, ResourceResponseMapper resourceResponseMapper, MinIOService minIOService, UserRepository userRepository, MeliSearchService meliSearchService, Index index) {
        super(resourceResponseMapper, resourceRepository);
        this.resourceRepository = resourceRepository;
        this.resourceResponseMapper = resourceResponseMapper;
        this.minIOService = minIOService;
        this.userRepository = userRepository;
        this.meliSearchService = meliSearchService;
    }

    public ResourceResponse save(UploadResourceRequest request, JwtAuthenticationToken auth) throws Exception {
        Integer userid = extractUserId(auth);
        var user = userRepository.findById(userid).orElseThrow(UnauthorizationException::new);
        String contextType = request.getSource().getContentType();
        Resource resource = new Resource(request.getTitle(), null, null, null, request.getIsPrivate(), contextType, user);
        MultipartFile file;
        log.info(contextType);
        if (contextType != null && contextType.startsWith("video")) {
            var result = uploadVideo(request);
            resource.setSource(result.getFirst().getSource());
            resource.setEngsub(result.getFirst().getEngsub());
            file = result.getSecond();
        } else if (contextType != null && contextType.startsWith("text")) {
            var result = uploadDocument(request);
            resource.setSource(result.getFirst().getSource());
            file = result.getSecond();
        } else {
            throw new BadRequestException("Invalid format file");
        }
        Resource newEntity = resourceRepository.save(resource);
        meliSearchService.addDocument(newEntity.getId(), newEntity.getTitle(), file.getInputStream(), newEntity.getCreatedAt(), request.getIsPrivate());
        return resourceResponseMapper.toDTO(newEntity);
    }

    Pair<Resource, MultipartFile> uploadVideo(UploadResourceRequest request) throws Exception {
        Resource resource = new Resource();
        if (request.getEn_sub().isEmpty()) throw new BadRequestException("Not found subtitle");
        if (request.getEn_sub().getSize() > 10000000) throw new BadRequestException("File size invalid");
        String filename = request.getTitle() + System.currentTimeMillis();
        String dirOfVideo = minIOService.uploadVideo(request.getSource(), filename);
        String dirOfSub = minIOService.uploadDocument(request.getEn_sub(), filename + "_en_sub");
        resource.setSource(dirOfVideo);
        resource.setEngsub(dirOfSub);
        return new Pair<>(resource, request.getEn_sub());
    }

    Pair<Resource, MultipartFile> uploadDocument(UploadResourceRequest request) throws Exception {
        Resource resource = new Resource();
        if (request.getEn_sub().getSize() > 10000000) throw new BadRequestException("File size invalid");
        String filename = request.getTitle() + System.currentTimeMillis();
        String dir = minIOService.uploadDocument(request.getSource(), filename);
        resource.setSource(dir);
        return new Pair<>(resource, request.getSource());
    }

    // https://www.meilisearch.com/docs/reference/api/search#search-parameters
    public List<ResourceResponse> search(ResourcesFilter request) {
        var results = meliSearchService.search(request.getSearchRequest()).getHits();
        var resources = new ArrayList<Resource>();
        results.forEach(e ->
                resources.add(resourceRepository.findById((int) Math.round((double) e.get("id"))).orElse(null)));
        return resourceResponseMapper.toListDTO(resources);
    }

    public InputStream getResources(Integer id) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        var resource = resourceRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return minIOService.getFile(resource.getSource());
    }

    public void deleteById(Integer id) {
        try {
            var resource = resourceRepository.findById(id).orElseThrow(EntityNotFoundException::new);
            // delete from minIO
            minIOService.delete(resource.getSource());
            if (resource.getEngsub() != null && !resource.getEngsub().isEmpty()) {
                minIOService.delete(resource.getEngsub());
            }
            if (resource.getVisub() != null && !resource.getVisub().isEmpty()) {
                minIOService.delete(resource.getVisub());
            }
            // delete from meliSearch
            meliSearchService.deleteById(id);
            // delete from db
            resourceRepository.deleteById(id);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }

    }

    public void deleteAll() {
        var id = resourceRepository.findAll();
        if (id != null) {
            for (var r : id) {
                deleteById(r.getId());
            }
        }
    }

    public ResourceResponse update(UpdateResourceRequest request, Integer id) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        var resource = resourceRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        resource.setTitle(request.getTitle());
        resource.setIsPrivate(request.getIsPrivate());
        meliSearchService.update(id, resource.getTitle(), resource.getIsPrivate());
        return resourceResponseMapper.toDTO(resource);
    }
}
