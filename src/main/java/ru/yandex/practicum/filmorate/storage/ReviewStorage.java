package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewStorage {
    Optional<Review> getReviewById(int id);

    List<Review> getReviewsByFilmId(int id);

    Review createReview(Review review);

    Review updateReview(Review review);

    void deleteReview(int id);

    void addLikeToReview(int userId, int reviewId);

    void addDislikeToReview(int userId, int reviewId);

    void removeLikeDislikeFromReview(int userId, int reviewId);

    List<Review> getReviews();
}
