package com.phatpl.learnvocabulary.services;

import com.phatpl.learnvocabulary.dtos.response.ResourceResponse;
import com.phatpl.learnvocabulary.mappers.ResourceResponseMapper;
import com.phatpl.learnvocabulary.models.graph.NVideo;
import com.phatpl.learnvocabulary.repositories.graph.VideoRepo;
import com.phatpl.learnvocabulary.repositories.jpa.ResourceRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SuggestionVideoService {
    VideoRepo videoRepo;
    UserService userService;
    ResourceRepository resourceRepository;
    ResourceResponseMapper resourceResponseMapper;

    @Autowired
    public SuggestionVideoService(VideoRepo videoRepo, UserService userService, ResourceRepository resourceRepository, ResourceResponseMapper resourceResponseMapper) {
        this.videoRepo = videoRepo;
        this.userService = userService;
        this.resourceRepository = resourceRepository;
        this.resourceResponseMapper = resourceResponseMapper;
    }

    List<NVideo> getSuggestionFromFriends() {
        var userId = userService.extractUserId();
        return videoRepo.getSuggestionFromFriends(Long.valueOf(userId));
    }

    List<NVideo> getSuggestionFromCategory(List<Long> categoryIds) {
        return videoRepo.getSuggestionFromCategory(categoryIds);
    }

    public List<ResourceResponse> getSuggestionVideos(List<Long> categoryIds) {
        var result = new ArrayList<ResourceResponse>();

        getSuggestionFromFriends().forEach(video -> {
            var elem = resourceRepository.findById(Math.toIntExact(video.getId()));
            elem.ifPresent(resource -> result.add(resourceResponseMapper.toDTO(resource)));
        });

        getSuggestionFromCategory(categoryIds).forEach(video -> {
            var elem = resourceRepository.findById(Math.toIntExact(video.getVideoId()));
            elem.ifPresent(resource -> result.add(resourceResponseMapper.toDTO(resource)));
        });

        // TODO: Bổ sung thêm video nếu chưa đủ số lượng video tối thiểu (20)

        return result;
    }
}
