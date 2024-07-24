package ru.yandex.practicum.filmorate.storage;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class DataBaseFilmStorage extends BaseStorage<Film> implements FilmStorage {

    private static final String INSERT_QUERY = "INSERT INTO film (name, description, release_date, duration, " +
            "mpa_id) VALUES (?, ?, ?, ?, ?)";
    private static final String INSERT_FILM_GENRES_QUERY = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
    private static final String FIND_BY_ID_QUERY =
            "SELECT f.*, " +
                    "m.name AS mpa_name, " +
                    "fg.genre_id AS genres_id, " +
                    "g.name AS genre_name, " +
                    "fl.user_id AS likes_id, " +
                    "fd.director_id AS directors_id, " +
                    "d.name AS director_name " +
                    "FROM film f " +
                    "LEFT JOIN mpa m ON f.mpa_id = m.id " +
                    "LEFT JOIN film_genres fg ON f.id = fg.film_id " +
                    "LEFT JOIN genre g ON fg.genre_id = g.id " +
                    "LEFT JOIN film_likes fl ON f.id = fl.film_id " +
                    "LEFT JOIN film_directors fd ON f.id = fd.film_id " +
                    "LEFT JOIN director d ON fd.director_id = d.id " +
                    "WHERE f.id = ?";
    private static final String FIND_ALL_QUERY =
            "SELECT  f.*, " +
                    "m.name AS mpa_name, " +
                    "fg.genre_id AS genres_id, " +
                    "g.name AS genre_name, " +
                    "fl.user_id AS likes_id, " +
                    "fd.director_id AS directors_id, " +
                    "d.name AS director_name " +
                    "FROM film f " +
                    "LEFT JOIN mpa m ON f.mpa_id = m.id " +
                    "LEFT JOIN film_genres fg ON f.id = fg.film_id " +
                    "LEFT JOIN genre g ON fg.genre_id = g.id " +
                    "LEFT JOIN film_likes fl ON f.id = fl.film_id " +
                    "LEFT JOIN film_directors fd ON f.id = fd.film_id " +
                    "LEFT JOIN director d ON fd.director_id = d.id ";

    private static final String FIND_FILM_DIRECTORS_BY_YEAR =
            "SELECT  f.*, " +
                    "EXTRACT(YEAR FROM f.release_date) AS release_year, " +
                    "m.name AS mpa_name, " +
                    "fg.genre_id AS genres_id, " +
                    "g.name AS genre_name, " +
                    "fl.user_id AS likes_id, " +
                    "fd.director_id AS directors_id, " +
                    "d.name AS director_name " +
                    "FROM film f " +
                    "LEFT JOIN mpa m ON f.mpa_id = m.id " +
                    "LEFT JOIN film_genres fg ON f.id = fg.film_id " +
                    "LEFT JOIN genre g ON fg.genre_id = g.id " +
                    "LEFT JOIN film_likes fl ON f.id = fl.film_id " +
                    "LEFT JOIN film_directors fd ON f.id = fd.film_id " +
                    "LEFT JOIN director d ON fd.director_id = d.id " +
                    "WHERE d.id = ? " +
                    "ORDER BY f.release_date ";

    private static final String FIND_FILM_DIRECTORS_BY_LIKES =
            "SELECT  f.*, " +
                    "COUNT(fl.user_id) AS cnt, " +
                    "m.name AS mpa_name, " +
                    "fg.genre_id AS genres_id, " +
                    "g.name AS genre_name, " +
                    "fl.user_id AS likes_id, " +
                    "fd.director_id AS directors_id, " +
                    "d.name AS director_name " +
                    "FROM film f " +
                    "LEFT JOIN mpa m ON f.mpa_id = m.id " +
                    "LEFT JOIN film_genres fg ON f.id = fg.film_id " +
                    "LEFT JOIN genre g ON fg.genre_id = g.id " +
                    "LEFT JOIN film_likes fl ON f.id = fl.film_id " +
                    "LEFT JOIN film_directors fd ON f.id = fd.film_id " +
                    "LEFT JOIN director d ON fd.director_id = d.id " +
                    "WHERE d.id = ? " +
                    "GROUP BY f.id, fl.user_id " +
                    "ORDER BY cnt DESC ";

    private static final String UPDATE_QUERY = "UPDATE film SET name = ?, description = ?, release_date = ?, " +
            "duration = ?, mpa_id = ? WHERE id = ?";
    private static final String ADD_LIKE_QUERY = "INSERT INTO film_likes (film_id, user_id) VALUES (?, ?)";
    private static final String DELETE_LIKE_QUERY = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";
    private static final String INSERT_FILM_DIRECTOR_QUERY = "INSERT INTO film_directors (film_id, director_id) " +
            "VALUES (?, ?)";
    private static final String DELETE_FILM_DIRECTORS_QUERY = "DELETE FROM film_directors WHERE film_id =?";

    private static final String DELETE_FILM_QUERY = "DELETE FROM film WHERE id = ?";
    private static final String DELETE_FILM_LIKE_QUERY = "DELETE FROM film_likes WHERE film_id = ?";
    private static final String DELETE_FILM_GENRE_QUERY = "DELETE FROM film_genres WHERE film_id = ?";
    private static final String DELETE_FILM_DIRECTOR_QUERY = "DELETE FROM film_directors WHERE film_id = ?";


    private static final String FIND_COMMON_FILMS_QUERY = "WITH common_films_id as (" +
            "SELECT fl1.film_id FROM film_likes fl1 " +
            "JOIN film_likes fl2 ON fl1.film_id = fl2.film_id " +
            "WHERE fl1.user_id = ? and fl2.user_id = ? ) " +
            FIND_ALL_QUERY +
            " WHERE f.id in (SELECT film_id FROM common_films_id)";

    private static final String FIND_LIKED_FILM_USER_QUERY = "WITH likedFilm AS (SELECT fl3.film_id FROM film_likes fl " +
            "JOIN FILM_LIKES fl2 ON fl2.film_id  = fl.film_id " +
            "JOIN FILM_LIKES fl3 ON fl3.user_id = fl2.user_id " +
            "WHERE fl.user_id = ? AND fl2.user_id != ?" +
            "AND fl3.film_id NOT IN (SELECT film_id FROM film_likes WHERE user_id = ? )) " +
            FIND_ALL_QUERY +
            " WHERE f.id in (SELECT film_id FROM likedFilm)";

    public DataBaseFilmStorage(JdbcTemplate jdbc, ResultSetExtractor<List<Film>> listExtractor) {
        super(listExtractor, jdbc);
    }


    @Override
    public Collection<Film> getAllFilms() {
        findManyExtractor(FIND_ALL_QUERY);
        return findManyExtractor(FIND_ALL_QUERY);
    }

    @Override
    public Optional<Film> findFilmById(Integer id) {
        return findOneExtractor(FIND_BY_ID_QUERY, id);
    }


    @Override
    public Film createFilm(Film film) {
        Integer id = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());
        film.setId(id);

        for (Genre genre : film.getGenres()) {
            insert(INSERT_FILM_GENRES_QUERY, id, genre.getId());
        }

        for (Director director : film.getDirectors()) {
            insert(INSERT_FILM_DIRECTOR_QUERY, id, director.getId());
        }

        return film;
    }

    @Override
    public Film updateFilm(Film updatedFilm) {
        update(
                UPDATE_QUERY,
                updatedFilm.getName(),
                updatedFilm.getDescription(),
                updatedFilm.getReleaseDate(),
                updatedFilm.getDuration(),
                updatedFilm.getMpa().getId(),
                updatedFilm.getId()
        );
        //обновление жанров
        delete(DELETE_FILM_GENRE_QUERY, updatedFilm.getId());

        for (Genre genre : updatedFilm.getGenres()) {
            insert(INSERT_FILM_GENRES_QUERY, updatedFilm.getId(), genre.getId());
        }

        //обновление режиссеров
        delete(DELETE_FILM_DIRECTORS_QUERY, updatedFilm.getId());
        for (Director director : updatedFilm.getDirectors()) {
            insert(INSERT_FILM_DIRECTOR_QUERY, updatedFilm.getId(), director.getId());
        }

        return updatedFilm;
    }

    @Override
    public Film addLike(Film film, Integer userId) {
        Integer filmId = film.getId();
        insert(ADD_LIKE_QUERY, filmId, userId);
        return film;
    }

    @Override
    public Film deleteLike(Film film, Integer userId) {
        Integer filmId = film.getId();
        delete(DELETE_LIKE_QUERY, filmId, userId);
        film = findFilmById(film.getId())
                .orElseThrow(() -> new NotFoundException("Не найден фильм с ID: " + filmId));
        return film;
    }

    @Override
    public Collection<Film> getPopularFilms(int count, Integer year, Integer genreId) {
        List<Film> films = getAllFilms().stream()
                .filter(film -> (year == null || film.getReleaseDate().getYear() == year))
                .filter(film -> (genreId == null ||
                        film.getGenres().stream().anyMatch(genre -> genre.getId().equals(genreId))))
                .sorted((f0, f1) -> f1.getLikes().size() - f0.getLikes().size())
                .limit(count)
                .sorted((f0, f1) -> f1.getId() - f0.getId())
                .toList();
        return films.reversed();
    }


    @Override
    public List<Film> getFilmsByDirector(String sortBy, Integer directorId) {
        if (sortBy.equalsIgnoreCase("year")) {
            return getDirectorFilmsByYear(directorId);
        }
        return getDirectorFilmsByLikes(directorId);
    }

    private List<Film> getDirectorFilmsByYear(Integer directorId) {
        findManyExtractor(FIND_FILM_DIRECTORS_BY_YEAR, directorId);
        return findManyExtractor(FIND_FILM_DIRECTORS_BY_YEAR, directorId).stream()
                .sorted(Comparator.comparing(Film::getReleaseDate))
                .toList();
    }

    private List<Film> getDirectorFilmsByLikes(Integer directorId) {
        findManyExtractor(FIND_FILM_DIRECTORS_BY_LIKES, directorId);
        return findManyExtractor(FIND_FILM_DIRECTORS_BY_LIKES, directorId);
    }

    @Override
    public Collection<Film> getCommonFilms(Integer userId, Integer friendId) {
        return findManyExtractor(FIND_COMMON_FILMS_QUERY, userId, friendId);
    }

    @Override
    public void deleteFilmById(int id) {
        delete(DELETE_FILM_LIKE_QUERY, id);
        delete(DELETE_FILM_GENRE_QUERY, id);
        delete(DELETE_FILM_DIRECTOR_QUERY, id);
        delete(DELETE_FILM_QUERY, id);
    }

    @Override
    public Collection<Film> search(String query, String by) {
        StringBuilder sb = new StringBuilder(FIND_ALL_QUERY);
        query = "%" + query + "%";

        if (by.contains("title") && by.contains("director")) {
            sb.append(" WHERE f.name LIKE ? OR d.name LIKE ?");
            return findManyExtractor(sb.toString(), query, query);

        } else if (by.contains("title")) {
            sb.append(" WHERE f.name LIKE ?");
            return findManyExtractor(sb.toString(), query);

        } else if (by.contains("director")) {
            sb.append(" WHERE d.name LIKE ?");
            return findManyExtractor(sb.toString(), query);

        } else {
            throw new ValidationException("Некорректные параметры запроса");
        }
    }

    @Override
    public Collection<Film> getLikedFilmsUser(Integer userId) {
        return findManyExtractor(FIND_LIKED_FILM_USER_QUERY, userId, userId, userId);
    }
}
