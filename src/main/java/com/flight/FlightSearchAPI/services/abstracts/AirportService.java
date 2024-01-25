package com.flight.FlightSearchAPI.services.abstracts;

import com.flight.FlightSearchAPI.dto.requests.AirportSaveRequest;
import com.flight.FlightSearchAPI.dto.requests.AirportUpdateRequest;
import com.flight.FlightSearchAPI.dto.responses.AirportResponse;
import com.flight.FlightSearchAPI.entities.Airport;

import java.util.List;

public interface AirportService {

    List<AirportResponse> getAllAirports();
    Airport getAirportById(long id);
    AirportResponse getAirportByCityName(String city);
    AirportResponse saveAirport(AirportSaveRequest airportSaveRequest);
    AirportResponse updateAirport(AirportUpdateRequest airportUpdateRequest);
    AirportResponse deleteAirport(long id);
}
