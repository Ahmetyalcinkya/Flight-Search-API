package com.flight.FlightSearchAPI.repositories;

import com.flight.FlightSearchAPI.entities.Authority;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class AuthorityRepositoryTest {

    private AuthorityRepository authorityRepository;

    @Autowired
    public AuthorityRepositoryTest(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Test
    void findByAuthority() {
        Authority authority = authorityRepository.findByAuthority("USER");
        assertNotNull(authority);
    }
    @Test
    void findByAuthorityFail() {
        Authority authority = authorityRepository.findByAuthority("STORE");
        assertNull(authority);
    }
}