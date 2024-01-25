package com.flight.FlightSearchAPI.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirportUpdateRequest {
    private long id;
    private String city;
}
