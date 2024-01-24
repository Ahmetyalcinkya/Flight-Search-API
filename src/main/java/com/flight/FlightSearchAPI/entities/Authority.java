package com.flight.FlightSearchAPI.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "authority", schema = "flight")
public class Authority { //TODO --> Implements GrantedAuthority

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "authority")
    private String authority;

    @OneToMany(mappedBy = "authority",cascade = CascadeType.ALL)
    private List<User> users;
}
