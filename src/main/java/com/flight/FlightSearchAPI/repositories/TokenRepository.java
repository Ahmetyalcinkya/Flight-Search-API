package com.flight.FlightSearchAPI.repositories;

import com.flight.FlightSearchAPI.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
}
