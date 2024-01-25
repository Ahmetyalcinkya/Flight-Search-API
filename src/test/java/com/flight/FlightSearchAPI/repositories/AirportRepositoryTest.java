package com.flight.FlightSearchAPI.repositories;

import com.flight.FlightSearchAPI.entities.Airport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class AirportRepositoryTest {

    private AirportRepository airportRepository;

    @Autowired
    public AirportRepositoryTest(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    @BeforeEach
    void setUp() {
        Airport airport = new Airport();
        airport.setCity("Istanbul");
        airportRepository.save(airport);
    }

    @Test
    void findAirportByCity() {
        Optional<Airport> airport = airportRepository.findAirportByCity("Istanbul");
        airport.ifPresent(value -> assertEquals("Istanbul", value.getCity()));
    }
    @Test
    void findAirportByCityFail() {
        Optional<Airport> airport = airportRepository.findAirportByCity("Ankara");
        assertEquals(Optional.empty(), airport);
    }
}