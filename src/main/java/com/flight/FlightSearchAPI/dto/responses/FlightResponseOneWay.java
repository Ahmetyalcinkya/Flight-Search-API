package com.flight.FlightSearchAPI.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightResponseOneWay {
    private long id;
    private LocalDateTime departureDate;
    private double price;
    private AirportResponse departureAirport;
    private AirportResponse arrivalAirport;
}
