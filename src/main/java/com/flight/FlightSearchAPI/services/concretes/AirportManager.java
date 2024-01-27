package com.flight.FlightSearchAPI.services.concretes;

import com.flight.FlightSearchAPI.dto.requests.AirportSaveRequest;
import com.flight.FlightSearchAPI.dto.requests.AirportUpdateRequest;
import com.flight.FlightSearchAPI.dto.responses.AirportResponse;
import com.flight.FlightSearchAPI.entities.Airport;
import com.flight.FlightSearchAPI.exceptions.FlightException;
import com.flight.FlightSearchAPI.repositories.AirportRepository;
import com.flight.FlightSearchAPI.services.abstracts.AirportService;
import com.flight.FlightSearchAPI.services.abstracts.ModelMapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AirportManager implements AirportService {

    private AirportRepository airportRepository;
    private ModelMapperService modelMapperService;

    @Autowired
    public AirportManager(AirportRepository airportRepository, ModelMapperService modelMapperService) {
        this.airportRepository = airportRepository;
        this.modelMapperService = modelMapperService;
    }

    @Override
    public List<AirportResponse> getAllAirports() {
        List<Airport> airports = airportRepository.findAll();

        return airports.stream().map(airport ->
                modelMapperService.forResponse().map(airport, AirportResponse.class)).collect(Collectors.toList());
    }

    @Override
    public Airport getAirportById(long id) {
        return airportRepository.findById(id).orElseThrow(() -> new FlightException("Airport not found!", HttpStatus.NOT_FOUND));
    }

    @Override
    public AirportResponse getAirportByCityName(String city) {
        Airport airport = airportRepository.findAirportByCity(city).orElseThrow(
                () -> new FlightException("Airport not found!", HttpStatus.NOT_FOUND));
        return modelMapperService.forResponse().map(airport, AirportResponse.class);
    }

    @Override
    public AirportResponse saveAirport(AirportSaveRequest airportSaveRequest) {
        Airport airport = modelMapperService.forRequest().map(airportSaveRequest, Airport.class);
        return modelMapperService.forResponse().map(airport, AirportResponse.class);
    }

    @Override
    public AirportResponse updateAirport(AirportUpdateRequest airportUpdateRequest) {
        Airport airport = modelMapperService.forRequest().map(airportUpdateRequest, Airport.class);
        return modelMapperService.forResponse().map(airport, AirportResponse.class);
    }

    @Override
    public AirportResponse deleteAirport(long id) {
        Airport airport = getAirportById(id);
        airportRepository.delete(airport);
        return modelMapperService.forResponse().map(airport, AirportResponse.class);
    }
}
