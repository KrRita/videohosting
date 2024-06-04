package com.example.videohosting.service;

import com.example.videohosting.entity.Category;
import com.example.videohosting.entity.Video;
import com.example.videohosting.mapper.VideoMapper;
import com.example.videohosting.model.VideoModel;
import com.example.videohosting.repository.AssessmentVideoRepository;
import com.example.videohosting.repository.CategoryRepository;
import com.example.videohosting.repository.ViewedVideoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class VideoServiceUtils {
    private final VideoMapper videoMapper;
    private final CategoryRepository categoryRepository;
    private final ViewedVideoRepository viewedVideoRepository;
    private final AssessmentVideoRepository assessmentVideoRepository;
    private final Logger logger = LoggerFactory.getLogger(VideoServiceUtils.class);

    @Autowired
    public VideoServiceUtils(VideoMapper videoMapper, CategoryRepository categoryRepository,
                             ViewedVideoRepository viewedVideoRepository,
                             AssessmentVideoRepository assessmentVideoRepository) {
        this.videoMapper = videoMapper;
        this.categoryRepository = categoryRepository;
        this.viewedVideoRepository = viewedVideoRepository;
        this.assessmentVideoRepository = assessmentVideoRepository;
    }

    public List<VideoModel> getVideoModelListWithCategories(List<Video> videos) {
        logger.info("Mapping videos to video models with categories");
        List<VideoModel> videoModels = videoMapper.toModelList(videos);
        Iterator<Video> videoIterator = videos.iterator();
        Iterator<VideoModel> videoModelIterator = videoModels.iterator();
        while (videoIterator.hasNext() && videoModelIterator.hasNext()) {
            List<Category> categories = videoIterator.next().getCategories();
            videoModelIterator.next().setCategories(toCategoryStringList(categories));
        }
        logger.info("Successfully mapped videos to video models with categories");
        return videoModels;
    }

    public List<Category> toCategoryEntityList(List<String> categories) {
        logger.info("Converting category names to category entities");
        List<Category> categoryEntities = new ArrayList<>();
        for (String category : categories) {
            categoryEntities.add(categoryRepository.getCategoryByName(category));
        }
        logger.info("Successfully converted category names to category entities");
        return categoryEntities;
    }

    public List<String> toCategoryStringList(List<Category> categories) {
        logger.info("Converting category entities to category names");
        List<String> categoryStrings = new ArrayList<>();
        if (categories == null) {
            return categoryStrings;
        }
        for (Category category : categories) {
            categoryStrings.add(category.getName());
        }
        logger.info("Successfully converted category entities to category names");
        return categoryStrings;
    }

    public List<VideoModel> addFieldInModelList(List<VideoModel> videoModels) {
        logger.info("Adding countViews, countLikes and countDislikes to video models");
        for (VideoModel videoModel : videoModels) {
            Long idVideo = videoModel.getIdVideo();
            Long countViews = viewedVideoRepository.countViewedVideosByVideo_IdVideo(idVideo);
            Long countLikes = assessmentVideoRepository.countAssessmentVideosByVideo_IdVideoAndLiked(idVideo, true);
            Long countDislikes = assessmentVideoRepository.countAssessmentVideosByVideo_IdVideoAndLiked(idVideo, false);
            videoModel.setCountViewing(countViews);
            videoModel.setCountLikes(countLikes);
            videoModel.setCountDislikes(countDislikes);
        }
        logger.info("Successfully added countViews, countLikes and countDislikes to video models");
        return videoModels;
    }

}
