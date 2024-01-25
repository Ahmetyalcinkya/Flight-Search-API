package com.flight.FlightSearchAPI.repositories;

import com.flight.FlightSearchAPI.entities.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query("SELECT f Flight f WHERE f.departureDate = :departureDate AND f.arrivalDate= :arrivalDate AND " +
            "f.arrivalAirport = (SELECT a.id FROM Airport a WHERE a.city = :arrivalAirport) AND" +
            "f.departureAirport = (SELECT a.id FROM Airport a WHERE a.city = :departureAirport)")
    List<Flight> searchTurnaroundFlights(LocalDateTime departureDate, LocalDateTime arrivalDate,
                                        String arrivalAirport, String departureAirport);

    @Query("SELECT f Flight f WHERE f.departureDate = :departureDate AND " +
            "f.arrivalAirport = (SELECT a.id FROM Airport a WHERE a.city = :arrivalAirport) AND" +
            "f.departureAirport = (SELECT a.id FROM Airport a WHERE a.city = :departureAirport)")
    List<Flight> searchOneWayFlights(LocalDateTime departureDate, String arrivalAirport, String departureAirport);
}
