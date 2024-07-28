package ru.yandex.practicum.filmorate.service.interfaces;

import ru.yandex.practicum.filmorate.dto.review.NewReviewRequest;
import ru.yandex.practicum.filmorate.dto.review.ReviewDto;
import ru.yandex.practicum.filmorate.dto.review.UpdatedReviewRequest;

import java.util.List;

public interface ReviewService {
    ReviewDto getReviewById(int id);

    List<ReviewDto> getReviewsByFilmId(Integer id, Integer count);

    ReviewDto createReview(NewReviewRequest request);

    ReviewDto updateReview(UpdatedReviewRequest request);

    void deleteReview(int id);

    void addLikeToReview(int userId, int reviewId);

    void addDislikeToReview(int userId, int reviewId);

    void removeLikeDislikeFromReview(int userId, int reviewId);
}
