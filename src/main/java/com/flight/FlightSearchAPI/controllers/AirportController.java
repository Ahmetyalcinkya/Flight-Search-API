package com.flight.FlightSearchAPI.controllers;

import com.flight.FlightSearchAPI.dto.requests.AirportSaveRequest;
import com.flight.FlightSearchAPI.dto.responses.AirportResponse;
import com.flight.FlightSearchAPI.dto.responses.FlightResponse;
import com.flight.FlightSearchAPI.entities.Airport;
import com.flight.FlightSearchAPI.entities.Flight;
import com.flight.FlightSearchAPI.repositories.AirportRepository;
import com.flight.FlightSearchAPI.services.abstracts.AirportService;
import com.flight.FlightSearchAPI.services.abstracts.FlightService;
import com.flight.FlightSearchAPI.services.abstracts.ModelMapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/airports")
public class AirportController {

    private AirportService airportService;
    private AirportRepository airportRepository;
    private FlightService flightService;
    private ModelMapperService modelMapperService;

    @Autowired
    public AirportController(AirportService airportService, FlightService flightService,
                             ModelMapperService modelMapperService, AirportRepository airportRepository) {
        this.airportService = airportService;
        this.flightService = flightService;
        this.modelMapperService = modelMapperService;
        this.airportRepository = airportRepository;
    }

    @GetMapping
    public List<AirportResponse> getAllAirports(){
        return airportService.getAllAirports();
    }
    @GetMapping("/{id}")
    public AirportResponse getAirportById(@PathVariable long id){
        Airport airport = airportService.getAirportById(id);
        return modelMapperService.forResponse().map(airport, AirportResponse.class);
    }
    @PostMapping("/admin/")
    public AirportResponse saveAirport(@RequestBody AirportSaveRequest airportSaveRequest){
        return airportService.saveAirport(airportSaveRequest);
    }
    @DeleteMapping("/admin/{id}")
    public AirportResponse deleteAirport(@PathVariable long id){
        getAirportById(id);
        return airportService.deleteAirport(id);
    }

}
