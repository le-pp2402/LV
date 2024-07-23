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
import com.phatpl.learnvocabulary.utils.FFmpegUtils;
import io.minio.errors.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.ServerException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
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
    UserService userService;


    @Autowired
    public ResourceService(ResourceRepository resourceRepository, ResourceResponseMapper resourceResponseMapper, MinIOService minIOService, UserRepository userRepository, MeliSearchService meliSearchService, Index index, UserService userService) {
        super(resourceResponseMapper, resourceRepository);
        this.resourceRepository = resourceRepository;
        this.resourceResponseMapper = resourceResponseMapper;
        this.minIOService = minIOService;
        this.userRepository = userRepository;
        this.meliSearchService = meliSearchService;
        this.userService = userService;
    }

    public ResourceResponse save(UploadResourceRequest req) throws Exception {
        Integer userid = userService.extractUserId();
        var user = userRepository.findById(userid).orElseThrow(UnauthorizationException::new);
        String contextType = req.getVideo().getContentType();

        Resource resource = new Resource();
        resource.setTitle(req.getTitle());
        resource.setIsPrivate(req.getIsPrivate());
        resource.setUser(user);

        if (contextType != null && contextType.startsWith("video")) {
            var mediaInfo = uploadVideo(req.getVideo(), req.getEnSub(), req.getViSub(), req.getThumbnail());

            resource.setVideo(mediaInfo.get("video"));
            resource.setViSub(mediaInfo.get("visub"));
            resource.setEnSub(mediaInfo.get("ensub"));
            resource.setThumbnail(mediaInfo.get("thumbnail"));

            var newElem = resourceRepository.save(resource);
            meliSearchService.addDocument(newElem.getId(), newElem.getTitle(), newElem.getCreatedAt(), newElem.getIsPrivate());

            return resourceResponseMapper.toDTO(newElem);
        } else {
            throw new BadRequestException("Invalid format file");
        }
    }


    private HashMap<String, String> uploadVideo(MultipartFile video, MultipartFile enSub, MultipartFile viSub, MultipartFile thumbnail) throws Exception {
        var baseDir = String.valueOf(System.currentTimeMillis());
        var mediaInfo = new HashMap<String, String>();

        var enSubPath = minIOService.uploadDocument(enSub, baseDir + "/subtitle/en");
        var viSubPath = minIOService.uploadDocument(viSub, baseDir + "/subtitle/vi");

        var chunkedFile = FFmpegUtils.ChunkVideoFile(video, "http://localhost:8080/video/" + baseDir + "/video/");
        chunkedFile.forEach((key, value) -> {
            try {
                var path = minIOService.uploadVideo(value, baseDir + "/video/" + key);
                if (key.equals("index.m3u8")) {
                    mediaInfo.put("video", path);
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        });

        if (thumbnail != null) {
            var thumbnailPath = minIOService.uploadDocument(thumbnail, baseDir + "/thumbnail");
            mediaInfo.put("thumbnail", thumbnailPath);
        }

        mediaInfo.put("ensub", enSubPath);
        mediaInfo.put("visub", viSubPath);

        return mediaInfo;
    }


    // https://www.meilisearch.com/docs/reference/api/search#search-parameters
    public List<ResourceResponse> search(ResourcesFilter request) {
        var results = meliSearchService.search(request.getSearchRequest()).getHits();
        var resources = new ArrayList<Resource>();
        results.forEach(e ->
                resources.add(resourceRepository.findById((int) Math.round((double) e.get("id"))).orElse(null)));
        return resourceResponseMapper.toListDTO(resources);
    }

    public void deleteById(Integer id) {
        try {
            var resource = resourceRepository.findById(id).orElseThrow(EntityNotFoundException::new);

            minIOService.delete(resource.getVideo());
            minIOService.delete(resource.getEnSub());
            minIOService.delete(resource.getViSub());

            meliSearchService.deleteById(id);

            resourceRepository.deleteById(id);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }

    }

    public void deleteAll() {
        meliSearchService.deleteAll();
        resourceRepository.deleteAll();
    }

    public ResourceResponse update(UpdateResourceRequest request, Integer id) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        var resource = resourceRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        resource.setTitle(request.getTitle());
        resource.setIsPrivate(request.getIsPrivate());
        meliSearchService.update(id, resource.getTitle(), resource.getIsPrivate());
        return resourceResponseMapper.toDTO(resource);
    }

    public InputStream getVideo(String name, String file) throws io.minio.errors.ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return minIOService.getFile(name + "/video/" + file);
    }
}