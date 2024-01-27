package com.flight.FlightSearchAPI.services.concretes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flight.FlightSearchAPI.dto.responses.AirportResponse;
import com.flight.FlightSearchAPI.dto.responses.FlightResponse;
import com.flight.FlightSearchAPI.entities.Airport;
import com.flight.FlightSearchAPI.entities.Flight;
import com.flight.FlightSearchAPI.exceptions.FlightException;
import com.flight.FlightSearchAPI.repositories.AirportRepository;
import com.flight.FlightSearchAPI.services.abstracts.FlightService;
import com.flight.FlightSearchAPI.services.abstracts.ModelMapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleService {

    private FlightService flightService;
    private AirportRepository airportRepository;
    private ModelMapperService modelMapperService;

    @Autowired
    public ScheduleService(FlightService flightService, AirportRepository airportRepository, ModelMapperService modelMapperService) {
        this.flightService = flightService;
        this.airportRepository = airportRepository;
        this.modelMapperService = modelMapperService;
    }
    public String fetch(){
        try {
            URL url = new URL("https://65b3a644770d43aba47a2c32.mockapi.io/flights/v1/flights");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer content = new StringBuffer();
            while(reader.readLine() != null ){
                content.append(reader.readLine());
            }
            reader.close();
            return content.toString();
        }catch (Exception e){
            throw new FlightException("Data not fetched!", HttpStatus.BAD_REQUEST);
        }
    }
    @Scheduled(cron = "0 8 * * * ?", zone = "Europe/Paris")
    public ResponseEntity<String> saveFlightsFromAPI(){

        try{
            String results = fetch();

            ObjectMapper objectMapper = new ObjectMapper();
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            objectMapper.registerModule(new JavaTimeModule());

            List<FlightResponse> apiFlights = objectMapper.readValue(results, typeFactory.constructCollectionType(List.class, FlightResponse.class));

            List<Flight> oneWayFlights = new ArrayList<>();
            List<Flight> twoWayFlights = new ArrayList<>();

            for(FlightResponse flightResponse: apiFlights){

                Airport arrivalAirport = airportRepository.findAirportByCity(flightResponse.getArrivalAirport().getCity()).orElseThrow(
                        () -> new FlightException("Airport not found!", HttpStatus.NOT_FOUND));
                Airport departureAirport = airportRepository.findAirportByCity(flightResponse.getDepartureAirport().getCity()).orElseThrow(
                        () -> new FlightException("Airport not found!", HttpStatus.NOT_FOUND));

                flightResponse.setArrivalAirport(modelMapperService.forResponse().map(arrivalAirport, AirportResponse.class));
                flightResponse.setDepartureAirport(modelMapperService.forResponse().map(departureAirport, AirportResponse.class));

                if(flightResponse.getReturnDate() == null){
                    oneWayFlights.add(modelMapperService.forResponse().map(flightResponse, Flight.class));
                }
                twoWayFlights.add(modelMapperService.forResponse().map(flightResponse, Flight.class));
            }
            flightService.saveOneWayFlightsFromAPI(oneWayFlights);
            flightService.saveTwoWayFlightsFromAPI(twoWayFlights);

            return ResponseEntity.ok("Flights saved!");
        } catch (Exception e) {
            throw new FlightException("An error occurred while flights saving!", HttpStatus.BAD_REQUEST);
        }
    }

}
