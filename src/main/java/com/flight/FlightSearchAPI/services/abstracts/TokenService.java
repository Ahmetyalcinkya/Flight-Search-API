package com.flight.FlightSearchAPI.services.abstracts;

import com.flight.FlightSearchAPI.entities.Token;
import org.springframework.security.core.Authentication;

public interface TokenService {
    String generateJwtToken(Authentication authentication);
    Token saveToken(Token jwtToken);
}
