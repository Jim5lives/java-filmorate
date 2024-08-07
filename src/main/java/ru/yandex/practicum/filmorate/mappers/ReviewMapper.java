package ru.yandex.practicum.filmorate.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.review.NewReviewRequest;
import ru.yandex.practicum.filmorate.dto.review.ReviewDto;
import ru.yandex.practicum.filmorate.dto.review.UpdatedReviewRequest;
import ru.yandex.practicum.filmorate.model.Review;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
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

    public static Review mapToReview(UpdatedReviewRequest request, Review review) {
        review.setContent(request.getContent());
        review.setPositive(request.getIsPositive());
        return review;
    }

}
