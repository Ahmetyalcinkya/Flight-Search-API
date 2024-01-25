package com.flight.FlightSearchAPI.repositories;

import com.flight.FlightSearchAPI.entities.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AirportRepository extends JpaRepository<Airport, Long> {

    @Query("SELECT a FROM Airport a WHERE a.city = :city")
    Airport findAirportByCity(String city);
}
