package com.flight.FlightSearchAPI.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightResponse {
    private long id;
    private LocalDateTime departureDate;
    private LocalDateTime returnDate;
    private double price;
    private AirportResponse departureAirport;
    private AirportResponse arrivalAirport;
}
