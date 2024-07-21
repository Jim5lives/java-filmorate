package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.NewReviewRequest;
import ru.yandex.practicum.filmorate.dto.ReviewDto;
import ru.yandex.practicum.filmorate.dto.UpdatedReviewRequest;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public ReviewDto createReview(@Valid @RequestBody NewReviewRequest request) {
        log.info("Получен запрос на добавление отзыва {}", request);
        return reviewService.createReview(request);
    }

    @PutMapping
    public ReviewDto updateReview(@Valid @RequestBody UpdatedReviewRequest request) {
        log.info("Получен запрос на обновление отзыва {}", request);
        return reviewService.updateReview(request);
    }

    @GetMapping
    public List<ReviewDto> getReviewsForFilm(@RequestParam(required = true) int filmId) {
        log.info("Получен запрос на список отзывов к фильму с ID = {}", filmId);
        return reviewService.getReviewsByFilmId(filmId);
    }

    @GetMapping("/{id}")
    public ReviewDto getReview(@PathVariable int id) {
        log.info("Получен запрос на отзыв с ID = {}", id);
        return reviewService.getReviewById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable int id) {
        log.info("Получен запрос на удаление отзыва с ID = {}", id);
        reviewService.deleteReview(id);
    }

    @PutMapping("/{reviewId}/like/{userId}")
    public void addLike(@PathVariable("reviewId") int reviewId, @PathVariable int userId) {
        log.info("Получен запрос добавление лайка к отзыву с ID = {} от пользователя с ID = {}", reviewId, userId);
        reviewService.addLikeToReview(userId, reviewId);
    }

    @DeleteMapping("/{reviewId}/like/{userId}")
    public void removeLike(@PathVariable("reviewId") int reviewId, @PathVariable int userId) {
        log.info("Получен запрос удаление лайка к отзыву с ID = {} от пользователя с ID = {}", reviewId, userId);
        reviewService.removeLikeDislikeFromReview(userId, reviewId);
    }

    @PutMapping("/{reviewId}/dislike/{userId}")
    public void addDislike(@PathVariable("reviewId") int reviewId, @PathVariable int userId) {
        log.info("Получен запрос добавление дизлайка к отзыву с ID = {} от пользователя с ID = {}", reviewId, userId);
        reviewService.addDislikeToReview(userId, reviewId);
    }

    @DeleteMapping("/{reviewId}/dislike/{userId}")
    public void removeDislike(@PathVariable("reviewId") int reviewId, @PathVariable int userId) {
        log.info("Получен запрос удаление дизлайка к отзыву с ID = {} от пользователя с ID = {}", reviewId, userId);
        reviewService.removeLikeDislikeFromReview(userId, reviewId);
    }

}
