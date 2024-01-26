package com.flight.FlightSearchAPI.dto.requests;

import com.flight.FlightSearchAPI.dto.responses.AirportResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightUpdateRequest {
    private long id;
    private LocalDateTime departureDate;
    private LocalDateTime returnDate;
    private double price;
    private AirportResponse departureAirport;
    private AirportResponse arrivalAirport;
}
