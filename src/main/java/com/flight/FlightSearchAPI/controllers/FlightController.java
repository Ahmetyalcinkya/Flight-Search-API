package com.flight.FlightSearchAPI.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flight.FlightSearchAPI.dto.requests.FlightSaveRequest;
import com.flight.FlightSearchAPI.dto.requests.FlightUpdateRequest;
import com.flight.FlightSearchAPI.dto.requests.OneWayFlightUpdateRequest;
import com.flight.FlightSearchAPI.dto.responses.FlightResponse;
import com.flight.FlightSearchAPI.dto.responses.FlightResponseOneWay;
import com.flight.FlightSearchAPI.entities.Flight;
import com.flight.FlightSearchAPI.exceptions.FlightException;
import com.flight.FlightSearchAPI.repositories.AirportRepository;
import com.flight.FlightSearchAPI.repositories.FlightRepository;
import com.flight.FlightSearchAPI.services.abstracts.AirportService;
import com.flight.FlightSearchAPI.services.abstracts.FlightService;
import com.flight.FlightSearchAPI.services.abstracts.ModelMapperService;
import com.flight.FlightSearchAPI.services.concretes.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/flights")
public class FlightController {

    private FlightService flightService;
    private FlightRepository flightRepository;
    private ScheduleService scheduleService;
    private AirportService airportService;
    private AirportRepository airportRepository;
    private ModelMapperService modelMapperService;

    @Autowired
    public FlightController(FlightService flightService, ScheduleService scheduleService,
                            AirportService airportService, AirportRepository airportRepository,
                            ModelMapperService modelMapperService, FlightRepository flightRepository) {
        this.flightService = flightService;
        this.scheduleService = scheduleService;
        this.airportService = airportService;
        this.airportRepository = airportRepository;
        this.modelMapperService = modelMapperService;
        this.flightRepository = flightRepository;
    }

    @GetMapping("/oneWay")
    public List<FlightResponseOneWay> getOneWayFlights(@RequestParam("departureDate") LocalDateTime departureDate,
                                                       @RequestParam("arrivalAirport") String arrivalAirport,
                                                       @RequestParam("departureAirport") String departureAirport){
        return flightService.getOneWayFlights(departureDate,departureAirport,arrivalAirport);
    }
    @GetMapping("/twoWay")
    public List<FlightResponse> getTwoWayFlights(@RequestParam("departureDate") LocalDateTime departureDate,
                                                 @RequestParam("returnDate") LocalDateTime returnDate,
                                                 @RequestParam("arrivalAirport") String arrivalAirport,
                                                 @RequestParam("departureAirport") String departureAirport){
        return flightService.getTwoWayFlights(departureDate,returnDate,departureAirport,arrivalAirport);
    }
    @GetMapping
    public List<FlightResponse> getAllFlights(){
        return flightService.getAllFlights();
    }
    @GetMapping("/{id}")
    public FlightResponse getFlightById(@PathVariable long id){
        return flightService.getFlightById(id);
    }
    @PostMapping("/admin/")
    private FlightResponse saveFlight(@RequestBody FlightSaveRequest flightSaveRequest){
        Flight flight = modelMapperService.forRequest().map(flightSaveRequest, Flight.class);
        flightRepository.save(flight);
        return modelMapperService.forResponse().map(flight, FlightResponse.class);
    }
    @PutMapping("/admin/")
    public FlightResponse updateFlight(@RequestBody FlightUpdateRequest flightUpdateRequest){
       if (flightUpdateRequest.getReturnDate() == null){
           OneWayFlightUpdateRequest oneWayFlightUpdateRequest = modelMapperService.forRequest().map(flightUpdateRequest, OneWayFlightUpdateRequest.class);
           return flightService.updateOneWayFlight(oneWayFlightUpdateRequest);
       }
           return flightService.updateTwoWayFlight(flightUpdateRequest);
    }
    @DeleteMapping("/admin/{id}")
    public FlightResponse deleteFlight(@PathVariable long id){
        getFlightById(id);
        return flightService.removeFlightById(id);
    }
    @GetMapping("/fetch")
    public ResponseEntity<String> fetchApi() throws JsonProcessingException {
        String results = scheduleService.fetch();
        String fetchedData;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            List<FlightResponse> apiFlights = objectMapper.readValue(results, typeFactory.constructCollectionType(List.class, FlightResponse.class));
            fetchedData = objectMapper.writeValueAsString(apiFlights);
        } catch (JsonProcessingException e) {
            throw new FlightException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(fetchedData);
    }

}
