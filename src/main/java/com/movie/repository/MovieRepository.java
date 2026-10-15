package com.movie.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.movie.model.Movie;

@Repository
public class MovieRepository {

    private final Map<Integer, Movie> movies = new HashMap<>();

    public Movie save(Movie movie) {
        movies.put(movie.getMovieId(), movie);
        return movie;
    }

    public Optional<Movie> findById(int movieId) {
        return Optional.ofNullable(movies.get(movieId));
    }

    public List<Movie> findAll() {
        return new ArrayList<>(movies.values());
    }

    public boolean existsById(int movieId) {
        return movies.containsKey(movieId);
    }

    public boolean deleteById(int movieId) {
        return movies.remove(movieId) != null;
    }

    public int count() {
        return movies.size();
    }
}
