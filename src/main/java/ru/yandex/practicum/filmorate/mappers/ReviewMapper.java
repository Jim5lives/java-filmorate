package ru.yandex.practicum.filmorate.mappers;

import ru.yandex.practicum.filmorate.dto.reviews.NewReviewRequest;
import ru.yandex.practicum.filmorate.dto.reviews.ReviewDto;
import ru.yandex.practicum.filmorate.dto.reviews.UpdatedReviewRequest;
import ru.yandex.practicum.filmorate.model.Review;

public class ReviewMapper {
    public static ReviewDto mapToDto(Review review) {
        return ReviewDto.builder()
                .reviewId(review.getId())
                .userId(review.getUserId())
                .filmId(review.getFilmId())
                .content(review.getContent())
                .isPositive(review.isPositive())
                .useful(review.getUseful()).build();
    }

    public static Review mapToReview(NewReviewRequest request) {
        return Review.builder()
                .userId(request.getUserId())
                .filmId(request.getFilmId())
                .content(request.getContent())
                .isPositive(request.getIsPositive()).build();
    }

    public static Review mapToReview(UpdatedReviewRequest request) {
        return Review.builder()
                .id(request.getReviewId())
                .userId(request.getUserId())
                .filmId(request.getFilmId())
                .content(request.getContent())
                .isPositive(request.getIsPositive()).build();
    }

}
