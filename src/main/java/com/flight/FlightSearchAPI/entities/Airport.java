package com.flight.FlightSearchAPI.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "airport", schema = "flight")
public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "city")
    private String city;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "departureAirport")
    private List<Flight> departureFlights;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "arrivalAirport")
    private List<Flight> arrivalFlights;

}
