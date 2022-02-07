package com.ssafy.a605.service;

import com.ssafy.a605.model.dto.ReviewDto;
import com.ssafy.a605.model.entity.Client;
import com.ssafy.a605.model.entity.Counselor;
import com.ssafy.a605.model.entity.Review;
import com.ssafy.a605.model.entity.Schedule;
import com.ssafy.a605.repository.ReviewRepository;
import com.ssafy.a605.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService{
    final private ScheduleRepository scheduleRepository;
    final private ReviewRepository reviewRepository;

    @Override
    public boolean writeReview(int scheduleId, ReviewDto reviewDto) {
        Schedule schedule = scheduleRepository.findById(scheduleId);
        Review review = new Review();
        review.setClient(schedule.getClient());
        review.setCounselor(schedule.getCounselor());
        review.setStartScore(reviewDto.getStartScore());
        review.setContent(reviewDto.getContent());
        Review ret = reviewRepository.save(review);
        return ret.equals(review);
    }

    @Override
    public boolean updateReview(ReviewDto reviewDto) {
        return false;
    }

    @Override
    public boolean deleteReview(int reviewId) {
        return false;
    }

    @Override
    public List<Review> getListReview(String counselor) {
        return null;
    }

    @Override
    public Review getReview(int reviewId) {
        return null;
    }

    @Override
    public boolean checkClient(String clientEmail, int scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId);
        if(schedule.getClient().getEmail().equals(clientEmail)) return true;
        return false;
    }
}
