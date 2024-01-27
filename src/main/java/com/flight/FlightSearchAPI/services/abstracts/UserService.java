package com.flight.FlightSearchAPI.services.abstracts;

import com.flight.FlightSearchAPI.dto.responses.UserResponse;
import com.flight.FlightSearchAPI.entities.Token;
import com.flight.FlightSearchAPI.entities.User;

import java.util.List;

public interface UserService {

    List<UserResponse> getAllUsers();
    UserResponse getUserById(long id);
    void getUserByEmail(String email);
    void updateUser(Token token, User user);
    UserResponse deleteUser(long id);
    int enableUser(String email);
    String getAuthenticatedUser();
}
