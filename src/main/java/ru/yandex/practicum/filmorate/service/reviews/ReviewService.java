package ru.yandex.practicum.filmorate.service.reviews;

import ru.yandex.practicum.filmorate.dto.reviews.NewReviewRequest;
import ru.yandex.practicum.filmorate.dto.reviews.ReviewDto;
import ru.yandex.practicum.filmorate.dto.reviews.UpdatedReviewRequest;

import java.util.List;

public interface ReviewService {
    ReviewDto getReviewById(int id);

    List<ReviewDto> getReviewsByFilmId(int id);

    ReviewDto createReview(NewReviewRequest request);

    ReviewDto updateReview(UpdatedReviewRequest request);

    void deleteReview(int id);

    void addLikeToReview(int userId, int reviewId);

    void addDislikeToReview(int userId, int reviewId);

    void removeLikeDislikeFromReview(int userId, int reviewId);
}
