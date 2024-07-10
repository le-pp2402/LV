package com.phatpl.learnvocabulary.services;

import com.meilisearch.sdk.Index;
import com.phatpl.learnvocabulary.dtos.request.UploadResourceRequest;
import com.phatpl.learnvocabulary.dtos.response.ResourceResponse;
import com.phatpl.learnvocabulary.exceptions.BadRequestException;
import com.phatpl.learnvocabulary.exceptions.UnauthorizationException;
import com.phatpl.learnvocabulary.filters.BaseFilter;
import com.phatpl.learnvocabulary.mappers.ResourceResponseMapper;
import com.phatpl.learnvocabulary.models.Resource;
import com.phatpl.learnvocabulary.repositories.ResourceRepository;
import com.phatpl.learnvocabulary.repositories.UserRepository;
import com.phatpl.learnvocabulary.utils.DefineDatatype.Pair;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Transactional
public class ResourceService extends BaseService<Resource, ResourceResponse, BaseFilter, Integer> {

    ResourceRepository resourceRepository;
    ResourceResponseMapper resourceResponseMapper;
    FileService fileService;
    UserRepository userRepository;
    MeliSearchService meliSearchService;
    Index index;

    @Autowired
    public ResourceService(ResourceRepository resourceRepository, ResourceResponseMapper resourceResponseMapper, FileService fileService, UserRepository userRepository, MeliSearchService meliSearchService, Index index) {
        super(resourceResponseMapper, resourceRepository);
        this.resourceRepository = resourceRepository;
        this.resourceResponseMapper = resourceResponseMapper;
        this.fileService = fileService;
        this.userRepository = userRepository;
        this.meliSearchService = meliSearchService;
        this.index = index;
    }

    public void save(UploadResourceRequest request, JwtAuthenticationToken auth) throws Exception {
        Integer userid = extractUserId(auth);
        var user = userRepository.findById(userid).orElseThrow(UnauthorizationException::new);
        String contextType = request.getSource().getContentType();
        Resource resource = new Resource(request.getTitle(), null, null, null, request.getIsPrivate(), user);
        MultipartFile file;
        log.info(contextType);
        if (contextType != null && contextType.startsWith("video")) {
            var result = uploadVideo(request);
            resource.setSource(result.getFirst().getSource());
            resource.setEnSub(result.getFirst().getEnSub());
            file = result.getSecond();
        } else if (contextType != null && contextType.startsWith("text")) {
            var result = uploadDocument(request);
            resource.setSource(result.getFirst().getSource());
            file = result.getSecond();
        } else {
            throw new BadRequestException("Invalid format file");
        }
        Resource newEntity = resourceRepository.save(resource);
        meliSearchService.addDocument(newEntity.getId(), newEntity.getTitle(), file);
    }

    Pair<Resource, MultipartFile> uploadVideo(UploadResourceRequest request) throws Exception {
        Resource resource = new Resource();
        if (request.getEn_sub().isEmpty()) throw new BadRequestException("Not found subtitle");
        if (request.getEn_sub().getSize() > 10000000) throw new BadRequestException("file size invalid");
        String dirOfVideo = fileService.uploadVideo(request.getSource(), request.getTitle());
        String dirOfSub = fileService.uploadDocument(request.getEn_sub(), request.getTitle() + "_en_sub");
        resource.setSource(dirOfVideo);
        resource.setSource(dirOfSub);
        return new Pair<>(resource, request.getEn_sub());
    }

    Pair<Resource, MultipartFile> uploadDocument(UploadResourceRequest request) throws Exception {
        Resource resource = new Resource();
        if (request.getEn_sub().getSize() > 10000000) throw new BadRequestException("file size invalid");
        String dir = fileService.uploadDocument(request.getSource(), request.getTitle());
        resource.setSource(dir);
        return new Pair<>(resource, request.getSource());
    }

    public List<ResourceResponse> search(String keyword) {
        var results = index.search(keyword).getHits();
        var resources = new ArrayList<Resource>();
        results.forEach(e ->
                resources.add(resourceRepository.findById((int) Math.round((double) e.get("id"))).orElse(null)));
        return resourceResponseMapper.toListDTO(resources);
    }
}
