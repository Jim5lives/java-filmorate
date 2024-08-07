package ru.yandex.practicum.filmorate.service.implementations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.review.NewReviewRequest;
import ru.yandex.practicum.filmorate.dto.review.ReviewDto;
import ru.yandex.practicum.filmorate.dto.review.UpdatedReviewRequest;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.mappers.ReviewMapper;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.interfaces.ReviewService;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewStorage reviewStorage;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Override
    public ReviewDto getReviewById(int id) {
        return reviewStorage.getReviewById(id).map(ReviewMapper::mapToDto).orElseThrow(() -> new NotFoundException("Review with id + " + id + "not found"));
    }

    @Override
    public List<ReviewDto> getReviewsByFilmId(Integer id, Integer count) {
        if (id == null) {
            if (count == null) {
                return reviewStorage.getReviews().stream()
                        .map(ReviewMapper::mapToDto).toList();
            } else {
                return reviewStorage.getReviews(count).stream()
                        .map(ReviewMapper::mapToDto).toList();
            }
        } else {
            if (count == null) {
                return reviewStorage.getReviewsByFilmId(id).stream()
                        .map(ReviewMapper::mapToDto).toList();
            } else {
                return reviewStorage.getReviewsByFilmId(id,count).stream()
                        .map(ReviewMapper::mapToDto).toList();
            }
        }
    }

    @Override
    public ReviewDto createReview(NewReviewRequest request) {
        Film film = filmStorage.findFilmById(request.getFilmId()).orElseThrow(() ->
                new NotFoundException("Couldn't add review to unexisting film with id = " + request.getFilmId()));
        User user = userStorage.findUserById(request.getUserId()).orElseThrow(() ->
                new NotFoundException("Couldn't add review from unexisting user with id = " + request.getUserId()));
        Review review = reviewStorage.createReview(ReviewMapper.mapToReview(request));

        userStorage.addEvent(user.getId(), review.getId(), EventType.REVIEW, OperationType.ADD);

        return ReviewMapper.mapToDto(review);
    }

    @Override
    public ReviewDto updateReview(UpdatedReviewRequest request) {
        Review review = reviewStorage.getReviewById(request.getReviewId()).orElseThrow(() ->
                new NotFoundException("Couldn't update unexisting review with id = " + request.getReviewId()));
        userStorage.addEvent(review.getUserId(), review.getId(), EventType.REVIEW, OperationType.UPDATE);
        return ReviewMapper.mapToDto(reviewStorage.updateReview(ReviewMapper.mapToReview(request, review)));
    }

    @Override
    public void deleteReview(int id) {
        Review review = reviewStorage.getReviewById(id).orElseThrow(() -> new ValidationException("Couldn't delete unexisting review with id = " + id));
        reviewStorage.deleteReview(id);
        userStorage.addEvent(review.getUserId(), review.getId(), EventType.REVIEW, OperationType.REMOVE);
    }

    @Override
    public void addLikeToReview(int userId, int reviewId) {
        User user = userStorage.findUserById(userId).orElseThrow(() -> new NotFoundException("Couldn't add like to review from unexisting user with id = " + userId));
        Review review = reviewStorage.getReviewById(reviewId).orElseThrow(() -> new NotFoundException("Couldn't add like unexisting review with id = " + reviewId));
        reviewStorage.addLikeToReview(userId, reviewId);
    }

    @Override
    public void addDislikeToReview(int userId, int reviewId) {
        User user = userStorage.findUserById(userId).orElseThrow(() -> new NotFoundException("Couldn't add dislike to review from unexisting user with id = " + userId));
        Review review = reviewStorage.getReviewById(reviewId).orElseThrow(() -> new NotFoundException("Couldn't add dislike unexisting review with id = " + reviewId));
        reviewStorage.addDislikeToReview(userId, reviewId);
    }

    @Override
    public void removeLikeDislikeFromReview(int userId, int reviewId) {
        User user = userStorage.findUserById(userId).orElseThrow(() -> new NotFoundException("Couldn't remove like/dislike to review from unexisting user with id = " + userId));
        Review review = reviewStorage.getReviewById(reviewId).orElseThrow(() -> new NotFoundException("Couldn't remove like/dislike unexisting review with id = " + reviewId));
        reviewStorage.removeLikeDislikeFromReview(userId, reviewId);
    }
}
