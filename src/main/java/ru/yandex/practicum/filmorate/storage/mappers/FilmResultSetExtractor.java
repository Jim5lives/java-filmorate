package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Component
public class FilmResultSetExtractor implements ResultSetExtractor<Film> {

    @Override
    public Film extractData(ResultSet rs) throws SQLException {
        Film film = null;
        Set<Genre> genres = new TreeSet<>(Comparator.comparingInt(Genre::getId));
        Set<Integer> likes = new HashSet<>();

        while (rs.next()) {
            if (film == null) {
                film = new Film();
                film.setId(rs.getInt("id"));
                film.setName(rs.getString("name"));
                film.setDescription(rs.getString("description"));
                film.setReleaseDate(rs.getDate("release_date").toLocalDate());
                film.setDuration(Duration.ofMinutes(rs.getInt("duration")));
                film.setGenres(genres);
                film.setLikes(likes);

                Mpa mpa = new Mpa();
                mpa.setId(rs.getInt("mpa_id"));
                mpa.setName(rs.getString("mpa_name"));
                film.setMpa(mpa);
            }

            int genreId = rs.getInt("genres_id");
            if (genreId != 0) {
                Genre genre = new Genre();
                genre.setId(genreId);
                genre.setName(rs.getString("genre_name"));
                genres.add(genre);
            }

            int likeId = rs.getInt("like_id");
            if (!rs.wasNull()) {
                likes.add(likeId);
            }
        }

        return film;
    }
}