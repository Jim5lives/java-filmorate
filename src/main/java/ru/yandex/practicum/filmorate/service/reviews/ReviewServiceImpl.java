package ru.yandex.practicum.filmorate.service.reviews;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.reviews.NewReviewRequest;
import ru.yandex.practicum.filmorate.dto.reviews.ReviewDto;
import ru.yandex.practicum.filmorate.dto.reviews.UpdatedReviewRequest;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.mappers.ReviewMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.reviews.ReviewStorage;

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
        return reviewStorage.getReviewById(id).map(ReviewMapper::mapToDto).orElseThrow(() ->
                new NotFoundException("Review with id + " + id + "not found"));
    }

    @Override
    public List<ReviewDto> getReviewsByFilmId(int id) {
        Film film = filmStorage.findFilmById(id).orElseThrow(() -> new ValidationException("Couldn't get reviews to" +
                " unexisting film with id = " + id));
        return reviewStorage.getReviewsByFilmId(id).stream().map(ReviewMapper::mapToDto).toList();
    }

    @Override
    public ReviewDto createReview(NewReviewRequest request) {

        Film film = filmStorage.findFilmById(request.getFilmId()).orElseThrow(() ->
                new NotFoundException("Couldn't add review to unexisting film with id = " + request.getFilmId()));
        User user = userStorage.findUserById(request.getUserId()).orElseThrow(() ->
                new NotFoundException("Couldn't add review from unexisting user with id = " + request.getUserId()));
        Review review = reviewStorage.createReview(ReviewMapper.mapToReview(request));
        return ReviewMapper.mapToDto(review);
    }

    @Override
    public ReviewDto updateReview(UpdatedReviewRequest request) {
        Film film = filmStorage.findFilmById(request.getFilmId()).orElseThrow(() ->
                new NotFoundException("Couldn't update review to unexisting film with id = " + request.getFilmId()));
        User user = userStorage.findUserById(request.getUserId()).orElseThrow(() ->
                new NotFoundException("Couldn't update review from unexisting user with id = " + request.getUserId()));
        Review review = reviewStorage.getReviewById(request.getReviewId()).orElseThrow(() ->
                new NotFoundException("Couldn't update unexisting review with id = " + request.getReviewId()));
        return ReviewMapper.mapToDto(reviewStorage.updateReview(ReviewMapper.mapToReview(request)));
    }

    @Override
    public void deleteReview(int id) {
        Review review = reviewStorage.getReviewById(id).orElseThrow(() ->
                new ValidationException("Couldn't delete unexisting review with id = " + id));
        reviewStorage.deleteReview(id);
    }

    @Override
    public void addLikeToReview(int userId, int reviewId) {
        User user = userStorage.findUserById(userId).orElseThrow(() ->
                new NotFoundException("Couldn't add like to review from unexisting user with id = " + userId));
        Review review = reviewStorage.getReviewById(reviewId).orElseThrow(() ->
                new NotFoundException("Couldn't add like unexisting review with id = " + reviewId));
        reviewStorage.addLikeToReview(userId, reviewId);
    }

    @Override
    public void addDislikeToReview(int userId, int reviewId) {
        User user = userStorage.findUserById(userId).orElseThrow(() ->
                new NotFoundException("Couldn't add dislike to review from unexisting user with id = " + userId));
        Review review = reviewStorage.getReviewById(reviewId).orElseThrow(() ->
                new NotFoundException("Couldn't add dislike unexisting review with id = " + reviewId));
        reviewStorage.addDislikeToReview(userId, reviewId);
    }

    @Override
    public void removeLikeDislikeFromReview(int userId, int reviewId) {
        User user = userStorage.findUserById(userId).orElseThrow(() ->
                new NotFoundException("Couldn't remove like/dislike to review from unexisting user with id = " + userId));
        Review review = reviewStorage.getReviewById(reviewId).orElseThrow(() ->
                new NotFoundException("Couldn't remove like/dislike unexisting review with id = " + reviewId));
        reviewStorage.removeLikeDislikeFromReview(userId, reviewId);
    }

}
