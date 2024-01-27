package com.flight.FlightSearchAPI.services.concretes;

import com.flight.FlightSearchAPI.dto.requests.FlightSaveRequest;
import com.flight.FlightSearchAPI.dto.requests.FlightUpdateRequest;
import com.flight.FlightSearchAPI.dto.requests.OneWayFlightSaveRequest;
import com.flight.FlightSearchAPI.dto.requests.OneWayFlightUpdateRequest;
import com.flight.FlightSearchAPI.dto.responses.AirportResponse;
import com.flight.FlightSearchAPI.dto.responses.FlightResponse;
import com.flight.FlightSearchAPI.dto.responses.FlightResponseOneWay;
import com.flight.FlightSearchAPI.entities.Airport;
import com.flight.FlightSearchAPI.entities.Flight;
import com.flight.FlightSearchAPI.exceptions.FlightException;
import com.flight.FlightSearchAPI.repositories.AirportRepository;
import com.flight.FlightSearchAPI.repositories.FlightRepository;
import com.flight.FlightSearchAPI.services.abstracts.FlightService;
import com.flight.FlightSearchAPI.services.abstracts.ModelMapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FlightManager implements FlightService {

    private FlightRepository flightRepository;
    private AirportRepository airportRepository;
    private ModelMapperService modelMapperService;

    @Autowired
    public FlightManager(FlightRepository flightRepository, AirportRepository airportRepository, ModelMapperService modelMapperService) {
        this.flightRepository = flightRepository;
        this.airportRepository = airportRepository;
        this.modelMapperService = modelMapperService;
    }

    @Override
    public List<FlightResponse> getAllFlights() {
        List<Flight> flights = flightRepository.findAll();

        return flights.stream().map(flight ->
                modelMapperService.forResponse().map(flight, FlightResponse.class)).collect(Collectors.toList());
    }

    @Override
    public List<FlightResponseOneWay> getOneWayFlights(LocalDateTime departureDate, String departureAirport, String arrivalAirport) {
        List<Flight> flights = flightRepository.searchOneWayFlights(departureDate, arrivalAirport, departureAirport);

        Airport departure = airportRepository.findAirportByCity(departureAirport).orElseThrow(
                () -> new FlightException("Airport not found!", HttpStatus.NOT_FOUND));
        Airport arrival = airportRepository.findAirportByCity(arrivalAirport).orElseThrow(
                () -> new FlightException("Airport not found!", HttpStatus.NOT_FOUND));

        AirportResponse departureResponse = modelMapperService.forResponse().map(departure, AirportResponse.class);
        AirportResponse arrivalResponse = modelMapperService.forResponse().map(arrival, AirportResponse.class);

        List<FlightResponseOneWay> flightResponseOneWays = new ArrayList<>();
        for(Flight flight: flights){
            FlightResponseOneWay flightResponse = modelMapperService.forResponse().map(flight, FlightResponseOneWay.class);
            flightResponse.setDepartureAirport(departureResponse);
            flightResponse.setArrivalAirport(arrivalResponse);
            flightResponseOneWays.add(flightResponse);
        }
        return flightResponseOneWays;
    }

    @Override
    public List<FlightResponse> getTwoWayFlights(LocalDateTime departureDate, LocalDateTime returnDate, String departureAirport, String arrivalAirport) {
        List<Flight> flights = flightRepository.searchTurnaroundFlights(departureDate,returnDate, arrivalAirport, departureAirport);

        Airport departure = airportRepository.findAirportByCity(departureAirport).orElseThrow(
                () -> new FlightException("Airport not found!", HttpStatus.NOT_FOUND));
        Airport arrival = airportRepository.findAirportByCity(arrivalAirport).orElseThrow(
                () -> new FlightException("Airport not found!", HttpStatus.NOT_FOUND));

        AirportResponse departureResponse = modelMapperService.forResponse().map(departure, AirportResponse.class);
        AirportResponse arrivalResponse = modelMapperService.forResponse().map(arrival, AirportResponse.class);

        List<FlightResponse> flightResponses = new ArrayList<>();
        for(Flight flight: flights){
            FlightResponse flightResponse = modelMapperService.forResponse().map(flight, FlightResponse.class);
            flightResponse.setDepartureAirport(departureResponse);
            flightResponse.setArrivalAirport(arrivalResponse);
            flightResponses.add(flightResponse);
        }
        return flightResponses;
    }

    @Override
    public List<FlightResponse> saveTwoWayFlightsFromAPI(List<Flight> flights) {
        return null; //TODO Make a new api
    }

    @Override
    public List<FlightResponse> saveOneWayFlightsFromAPI(List<Flight> flights) {
        return null; //TODO Make a new api
    }

    @Override
    public FlightResponse getFlightById(long id) {
        Optional<Flight> flight = flightRepository.findById(id);

        if(flight.isPresent()){
            return modelMapperService.forResponse().map(flight, FlightResponse.class);
        }
        throw new FlightException("Flight not found!", HttpStatus.NOT_FOUND);
    }

    @Override
    public FlightResponse updateOneWayFlight(OneWayFlightUpdateRequest oneWayFlightUpdateRequest) {
        return null; //TODO Update will be added
    }

    @Override
    public FlightResponse updateTwoWayFlight(FlightUpdateRequest flightUpdateRequest) {
        return null; //TODO Update will be added
    }

    @Override
    public FlightResponse removeFlightById(long id) {
        FlightResponse flightResponse = getFlightById(id);
        Flight flight = modelMapperService.forRequest().map(flightResponse, Flight.class);
        flightRepository.delete(flight);
        return modelMapperService.forResponse().map(flight, FlightResponse.class);
    }
}
