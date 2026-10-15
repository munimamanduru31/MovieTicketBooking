package com.movie;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.movie.config.AppConfig;
import com.movie.model.Movie;
import com.movie.service.MovieService;

public class Main {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        MovieService service = context.getBean(MovieService.class);
        Scanner sc = new Scanner(System.in);

        boolean running = true;

        try {
            while (running) {

                System.out.println("\n===== MOVIE TICKET BOOKING SYSTEM =====");
                System.out.println("1. Add Movie");
                System.out.println("2. View All Movies");
                System.out.println("3. Search Movie by ID");
                System.out.println("4. Search Movie by Name");
                System.out.println("5. Book Tickets");
                System.out.println("6. Sort Movies by Ticket Price");
                System.out.println("7. Update Ticket Price");
                System.out.println("8. Update Movie Using PATCH");
                System.out.println("9. Delete Movie");
                System.out.println("10. View Statistics");
                System.out.println("11. Exit");
                System.out.print("Enter choice: ");

                int choice;

                try {
                    choice = Integer.parseInt(sc.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number.");
                    continue;
                }

                try {
                    switch (choice) {

                    case 1: {
                        System.out.print("Movie ID: ");
                        int id = Integer.parseInt(sc.nextLine().trim());

                        System.out.print("Movie name: ");
                        String name = sc.nextLine();

                        System.out.print("Genre: ");
                        String genre = sc.nextLine();

                        System.out.print("Ticket price: ");
                        double price = Double.parseDouble(sc.nextLine().trim());

                        System.out.print("Available seats: ");
                        int seats = Integer.parseInt(sc.nextLine().trim());

                        Movie movie = new Movie(
                                id, name, genre, price, seats, 0);

                        service.addMovie(movie);
                        System.out.println("Movie added successfully.");
                        break;
                    }

                    case 2:
                        service.getAllMovies().forEach(System.out::println);
                        break;

                    case 3: {
                        System.out.print("Enter movie ID: ");
                        int id = Integer.parseInt(sc.nextLine().trim());

                        System.out.println(service.getMovieById(id)
                                .orElse(null));
                        break;
                    }

                    case 4: {
                        System.out.print("Enter movie name: ");
                        String name = sc.nextLine();

                        service.searchByName(name)
                                .forEach(System.out::println);
                        break;
                    }

                    case 5: {
                        System.out.print("Enter movie ID: ");
                        int id = Integer.parseInt(sc.nextLine().trim());

                        System.out.print("Number of tickets: ");
                        int seats = Integer.parseInt(sc.nextLine().trim());

                        Movie booked = service.bookTickets(id, seats);

                        double total = booked.getTicketPrice() * seats;

                        System.out.println("Booking successful!");
                        System.out.println("Movie: " + booked.getMovieName());
                        System.out.println("Tickets: " + seats);
                        System.out.println("Total amount: " + total);
                        System.out.println("Remaining seats: "
                                + booked.getAvailableSeats());
                        break;
                    }

                    case 6:
                        service.sortByTicketPrice()
                                .forEach(System.out::println);
                        break;

                    case 7: {
                        System.out.print("Enter movie ID: ");
                        int id = Integer.parseInt(sc.nextLine().trim());

                        System.out.print("New ticket price: ");
                        double price = Double.parseDouble(sc.nextLine().trim());

                        System.out.println(service.updateTicketPrice(id, price));
                        System.out.println("Ticket price updated.");
                        break;
                    }

                    case 8: {
                        System.out.print("Enter movie ID: ");
                        int id = Integer.parseInt(sc.nextLine().trim());

                        System.out.println("1. Movie name");
                        System.out.println("2. Genre");
                        System.out.println("3. Ticket price");
                        System.out.println("4. Available seats");
                        System.out.print("Choose field: ");

                        int field = Integer.parseInt(sc.nextLine().trim());
                        Map<String, Object> updates = new HashMap<>();

                        switch (field) {
                        case 1:
                            System.out.print("New movie name: ");
                            updates.put("movieName", sc.nextLine());
                            break;
                        case 2:
                            System.out.print("New genre: ");
                            updates.put("genre", sc.nextLine());
                            break;
                        case 3:
                            System.out.print("New ticket price: ");
                            updates.put("ticketPrice",
                                    Double.parseDouble(sc.nextLine().trim()));
                            break;
                        case 4:
                            System.out.print("New available seats: ");
                            updates.put("availableSeats",
                                    Integer.parseInt(sc.nextLine().trim()));
                            break;
                        default:
                            System.out.println("Invalid field choice.");
                        }

                        if (!updates.isEmpty()) {
                            System.out.println(service.patchMovie(id, updates));
                            System.out.println("Movie updated successfully.");
                        }
                        break;
                    }

                    case 9: {
                        System.out.print("Enter movie ID to delete: ");
                        int id = Integer.parseInt(sc.nextLine().trim());

                        if (service.deleteMovie(id)) {
                            System.out.println("Movie deleted successfully.");
                        } else {
                            System.out.println("Movie ID not found.");
                        }
                        break;
                    }

                    case 10:
                        System.out.println("Total movies: "
                                + service.getTotalMovies());
                        System.out.println("Average ticket price: "
                                + service.getAverageTicketPrice());
                        break;

                    case 11:
                        running = false;
                        System.out.println("Thank you for using the system.");
                        break;

                    default:
                        System.out.println("Invalid choice.");
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Invalid number. Please try again.");
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Operation failed: " + e.getMessage());
                }
            }
        } finally {
            sc.close();
            context.close();
        }
    }
}
