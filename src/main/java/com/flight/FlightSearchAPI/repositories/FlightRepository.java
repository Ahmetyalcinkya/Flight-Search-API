package com.flight.FlightSearchAPI.repositories;

import com.flight.FlightSearchAPI.entities.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query("SELECT f FROM Flight f WHERE f.departureDate = :departureDate AND f.returnDate = :returnDate AND " +
            "f.arrivalAirport.id = (SELECT a.id FROM Airport a WHERE a.city = :arrivalAirport) AND " +
            "f.departureAirport.id = (SELECT a.id FROM Airport a WHERE a.city = :departureAirport)")
    List<Flight> searchTurnaroundFlights(@RequestParam("departureDate") LocalDateTime departureDate, @RequestParam("returnDate") LocalDateTime returnDate,
                                         @RequestParam("arrivalAirport") String arrivalAirport, @RequestParam("departureAirport") String departureAirport);

    @Query("SELECT f FROM Flight f WHERE f.departureDate = :departureDate AND " +
            "f.arrivalAirport.id = (SELECT a.id FROM Airport a WHERE a.city = :arrivalAirport) AND " +
            "f.departureAirport.id = (SELECT a.id FROM Airport a WHERE a.city = :departureAirport)")
    List<Flight> searchOneWayFlights(@RequestParam("departureDate") LocalDateTime departureDate, @RequestParam("arrivalAirport") String arrivalAirport,
                                     @RequestParam("departureAirport") String departureAirport);
}
