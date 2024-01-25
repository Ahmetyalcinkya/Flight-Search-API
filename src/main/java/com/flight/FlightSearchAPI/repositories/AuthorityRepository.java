package com.flight.FlightSearchAPI.repositories;

import com.flight.FlightSearchAPI.entities.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    @Query("SELECT a FROM Authority a WHERE a.authority = :authority")
    Authority findByAuthority(String authority);
}
