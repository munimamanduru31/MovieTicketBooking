package com.movie.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.movie.model.Movie;
import com.movie.repository.MovieRepository;

import jakarta.validation.Validator;

@Service
public class MovieService {

    private final MovieRepository repository;
    private final Validator validator;

    public MovieService(MovieRepository repository, Validator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public Movie addMovie(Movie movie) {
        validateMovie(movie);

        if (repository.existsById(movie.getMovieId())) {
            throw new IllegalArgumentException("Movie ID already exists.");
        }

        return repository.save(movie);
    }

    public List<Movie> getAllMovies() {
        return repository.findAll();
    }

    public Optional<Movie> getMovieById(int movieId) {
        return repository.findById(movieId);
    }

    public List<Movie> searchByName(String name) {
        return repository.findAll().stream()
                .filter(movie -> movie.getMovieName() != null
                        && movie.getMovieName().toLowerCase()
                                .contains(name.toLowerCase()))
                .toList();
    }

    public List<Movie> sortByTicketPrice() {
        return repository.findAll().stream()
                .sorted(Comparator.comparingDouble(Movie::getTicketPrice))
                .toList();
    }

    public Movie bookTickets(int movieId, int seats) {
        if (seats <= 0) {
            throw new IllegalArgumentException(
                    "Number of seats must be positive.");
        }

        Movie movie = repository.findById(movieId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Movie not found."));

        if (movie.getAvailableSeats() < seats) {
            throw new IllegalArgumentException(
                    "Not enough seats available.");
        }

        movie.setAvailableSeats(movie.getAvailableSeats() - seats);
        movie.setTotalBookings(movie.getTotalBookings() + seats);

        repository.save(movie);
        return movie;
    }

    public Movie updateTicketPrice(int movieId, double newPrice) {
        Movie movie = repository.findById(movieId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Movie not found."));

        movie.setTicketPrice(newPrice);
        validateMovie(movie);

        return repository.save(movie);
    }

    public Movie patchMovie(int movieId, Map<String, Object> updates) {
        Movie existing = repository.findById(movieId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Movie not found."));

        Movie updated = new Movie(
                existing.getMovieId(),
                existing.getMovieName(),
                existing.getGenre(),
                existing.getTicketPrice(),
                existing.getAvailableSeats(),
                existing.getTotalBookings());

        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            switch (entry.getKey()) {
                case "movieName":
                    updated.setMovieName((String) entry.getValue());
                    break;
                case "genre":
                    updated.setGenre((String) entry.getValue());
                    break;
                case "ticketPrice":
                    updated.setTicketPrice(
                            ((Number) entry.getValue()).doubleValue());
                    break;
                case "availableSeats":
                    updated.setAvailableSeats(
                            ((Number) entry.getValue()).intValue());
                    break;
                default:
                    throw new IllegalArgumentException(
                            "Unknown field: " + entry.getKey());
            }
        }

        validateMovie(updated);
        return repository.save(updated);
    }

    public boolean deleteMovie(int movieId) {
        return repository.deleteById(movieId);
    }

    public int getTotalMovies() {
        return repository.count();
    }

    public double getAverageTicketPrice() {
        return repository.findAll().stream()
                .mapToDouble(Movie::getTicketPrice)
                .average()
                .orElse(0.0);
    }

    private void validateMovie(Movie movie) {
        var violations = validator.validate(movie);

        if (!violations.isEmpty()) {
            String message = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .reduce((a, b) -> a + "; " + b)
                    .orElse("Invalid movie details.");

            throw new IllegalArgumentException(message);
        }
    }
}
