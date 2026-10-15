package com.movie.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Min;

public class Movie {

    @Positive
    private int movieId;

    @NotBlank(message = "Movie name cannot be empty")
    private String movieName;

    @NotBlank(message = "Genre cannot be empty")
    private String genre;

    @Positive
    private double ticketPrice;

    @Min(value = 1, message = "Available seats must be at least 1")
    private int availableSeats;

    @PositiveOrZero
    private int totalBookings;

    public Movie() {
    }

    public Movie(int movieId, String movieName, String genre,
                 double ticketPrice, int availableSeats,
                 int totalBookings) {
        this.movieId = movieId;
        this.movieName = movieName;
        this.genre = genre;
        this.ticketPrice = ticketPrice;
        this.availableSeats = availableSeats;
        this.totalBookings = totalBookings;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public int getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(int totalBookings) {
        this.totalBookings = totalBookings;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "movieId=" + movieId +
                ", movieName='" + movieName + '\'' +
                ", genre='" + genre + '\'' +
                ", ticketPrice=" + ticketPrice +
                ", availableSeats=" + availableSeats +
                ", totalBookings=" + totalBookings +
                '}';
    }
}
