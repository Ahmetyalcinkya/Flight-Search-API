package com.flight.FlightSearchAPI.services.abstracts;

import com.flight.FlightSearchAPI.dto.requests.FlightSaveRequest;
import com.flight.FlightSearchAPI.dto.requests.FlightUpdateRequest;
import com.flight.FlightSearchAPI.dto.requests.OneWayFlightSaveRequest;
import com.flight.FlightSearchAPI.dto.requests.OneWayFlightUpdateRequest;
import com.flight.FlightSearchAPI.dto.responses.FlightResponse;
import com.flight.FlightSearchAPI.dto.responses.FlightResponseOneWay;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightService {

    List<FlightResponse> getAllFlights();
    List<FlightResponseOneWay> getOneWayFlights(LocalDateTime departureDate, String departureAirport, String arrivalAirport);
    List<FlightResponse> getTwoWayFlights(LocalDateTime departureDate, LocalDateTime returnDate, String departureAirport, String arrivalAirport);
    List<FlightResponse> saveTwoWayFlightsFromAPI(List<FlightSaveRequest> flightSaveRequests);
    List<FlightResponse> saveOneWayFlightsFromAPI(List<OneWayFlightSaveRequest> oneWayFlightSaveRequests);
    FlightResponse getFlightById(long id);
    FlightResponse updateOneWayFlight(OneWayFlightUpdateRequest oneWayFlightUpdateRequest);
    FlightResponse updateTwoWayFlight(FlightUpdateRequest flightUpdateRequest);
    FlightResponse removeFlightById(long id);
}
