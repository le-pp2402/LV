package com.phatpl.learnvocabulary.services;

import com.phatpl.learnvocabulary.dtos.request.AttachContainCategory;
import com.phatpl.learnvocabulary.dtos.request.CategoryRequest;
import com.phatpl.learnvocabulary.models.graph.NCategory;
import com.phatpl.learnvocabulary.models.graph.relationship.ContainCategory;
import com.phatpl.learnvocabulary.repositories.graph.CategoryRepo;
import com.phatpl.learnvocabulary.repositories.graph.ContainCategoryRepo;
import com.phatpl.learnvocabulary.repositories.graph.VideoRepo;
import com.phatpl.learnvocabulary.utils.Constant;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {
    CategoryRepo categoryRepo;
    VideoRepo videoRepo;
    ContainCategoryRepo containCategoryRepo;

    @Autowired
    public CategoryService(CategoryRepo categoryRepo, VideoRepo videoRepo, ContainCategoryRepo containCategoryRepo, ContainCategoryRepo containCategoryRepo1) {
        this.categoryRepo = categoryRepo;
        this.videoRepo = videoRepo;
        this.containCategoryRepo = containCategoryRepo1;
    }

    public NCategory addCategory(CategoryRequest req) {
        NCategory category = new NCategory(req.getCategory());
        return categoryRepo.save(category);
    }

    public void delCategory(Long id) {
        categoryRepo.deleteById(id);
    }

    public NCategory updCategory(Long id, CategoryRequest req) {
        var category = categoryRepo.findById(id);

        if (category.isEmpty()) throw new EntityNotFoundException(Constant.CATEGORY_NOT_FOUND);

        category.get().setCategory(req.getCategory());
        categoryRepo.save(category.get());
        return category.get();
    }


    public void detachFromVideo(Long videoId, DetachCategoryRequest req) {
        var video = videoRepo.findByVideoId(videoId);
        var category = categoryRepo.findById(req.getCategoryId());

        if (video.isEmpty()) {
            throw new EntityNotFoundException(Constant.VIDEO_NOT_FOUND);
        }

        if (category.isEmpty()) {
            throw new EntityNotFoundException(Constant.CATEGORY_NOT_FOUND);
        }
        var result = containCategoryRepo.findByVideoIdAndCategoryId(videoId, req.getCategoryId());

        result.ifPresent(containCategoryRepo::deleteById);
    }

    public ContainCategory attachToVideo(Long videoId, AttachContainCategory req) {
        var video = videoRepo.findByVideoId(videoId);
        var category = categoryRepo.findById(req.getCategoryId());

        if (video.isEmpty()) {
            throw new EntityNotFoundException(Constant.VIDEO_NOT_FOUND);
        }

        if (category.isEmpty()) {
            throw new EntityNotFoundException(Constant.CATEGORY_NOT_FOUND);
        }

        var chkExisted = containCategoryRepo.findByVideoIdAndCategoryId(videoId, req.getCategoryId());
        if (chkExisted.isPresent()) {
            return containCategoryRepo.findById(chkExisted.get()).get();
        }

        ContainCategory rel = new ContainCategory();
        rel.setVideo(video.get());
        rel.setCategory(category.get());

        return containCategoryRepo.save(rel);
    }

    public List<NCategory> findAll() {
        return categoryRepo.findAll();
    }

    public List<NCategory> findByVideoId(Long videoId) {
        return categoryRepo.findByVideoId(videoId);
    }
}
