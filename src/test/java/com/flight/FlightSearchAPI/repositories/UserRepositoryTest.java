package com.flight.FlightSearchAPI.repositories;

import com.flight.FlightSearchAPI.entities.Authority;
import com.flight.FlightSearchAPI.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserRepositoryTest {

    private UserRepository userRepository;
    private AuthorityRepository authorityRepository;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }

    @Test
    void findUserByEmail() {
        Optional<User> user = userRepository.findUserByEmail("ahmetcan.yalcinkaya55@gmail.com");
        assertNotNull(user.get());
    }
    @Test
    void findUserByEmailFail() {
        String nonExistingEmail = "xyz@gmail.com";
        Optional<User> user = userRepository.findUserByEmail(nonExistingEmail);
        assertTrue(user.isEmpty());
    }

    @Test
    void enableUser() {
        Optional<User> user = userRepository.findUserByEmail("ahmetcan.yalcinkaya55@gmail.com");

        if(user.isPresent()){
            userRepository.enableUser(user.get().getEmail());
        }
    }
    @Test
    void enableUserFail() {
        String nonExistingEmail = "xyz@gmail.com";
        Optional<User> existingUser = userRepository.findUserByEmail(nonExistingEmail);

        if(existingUser.isEmpty()){
            userRepository.enableUser(nonExistingEmail);
        }
    }
}