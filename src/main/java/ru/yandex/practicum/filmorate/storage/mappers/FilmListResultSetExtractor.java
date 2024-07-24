package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.*;

@Component
public class FilmListResultSetExtractor implements ResultSetExtractor<List<Film>> {

    @Override
    public List<Film> extractData(ResultSet rs) throws SQLException {
        Map<Integer, Film> filmMap = new HashMap<>();
        Map<Integer, Set<Genre>> genreMap = new HashMap<>();
        Map<Integer, Set<Integer>> likeMap = new HashMap<>();
        Map<Integer, Set<Director>> directorsMap = new HashMap<>();

        while (rs.next()) {
            int filmId = rs.getInt("id");
            Film film = filmMap.get(filmId);
            if (film == null) {
                film = new Film();
                film.setId(filmId);
                film.setName(rs.getString("name"));
                film.setDescription(rs.getString("description"));
                film.setReleaseDate(rs.getDate("release_date").toLocalDate());
                film.setDuration(Duration.ofMinutes(rs.getInt("duration")));
                film.setGenres(new HashSet<>());
                film.setLikes(new HashSet<>());

                Mpa mpa = new Mpa();
                mpa.setId(rs.getInt("mpa_id"));
                mpa.setName(rs.getString("mpa_name"));
                film.setMpa(mpa);

                filmMap.put(filmId, film);
            }

            int genreId = rs.getInt("genres_id");
            if (genreId != 0) {
                Genre genre = new Genre();
                genre.setId(genreId);
                genre.setName(rs.getString("genre_name"));
                genreMap.computeIfAbsent(filmId, k -> new TreeSet<>(Comparator.comparingInt(Genre::getId))).add(genre);
            }

            int likeId = rs.getInt("likes_id");
            if (!rs.wasNull()) {
                likeMap.computeIfAbsent(filmId, k -> new HashSet<>()).add(likeId);
            }

            int directorId = rs.getInt("director_id");
            if (directorId != 0) {
                Director director = new Director();
                director.setId(directorId);
                director.setName(rs.getString("director_name"));
                directorsMap.computeIfAbsent(filmId, k ->
                        new TreeSet<>(Comparator.comparingInt(Director::getId))).add(director);
            }
        }


        for (Map.Entry<Integer, Film> entry : filmMap.entrySet()) {
            int filmId = entry.getKey();
            Film film = entry.getValue();
            film.setGenres(genreMap.getOrDefault(filmId, Collections.emptySet()));
            film.setLikes(likeMap.getOrDefault(filmId, Collections.emptySet()));
            film.setDirectors(directorsMap.getOrDefault(filmId, Collections.emptySet()));
        }

        return new ArrayList<>(filmMap.values());
    }
}