package com.flight.FlightSearchAPI.controllers;

import com.flight.FlightSearchAPI.dto.requests.LoginRequest;
import com.flight.FlightSearchAPI.dto.requests.UserSaveRequest;
import com.flight.FlightSearchAPI.dto.responses.LoginResponse;
import com.flight.FlightSearchAPI.dto.responses.UserResponse;
import com.flight.FlightSearchAPI.services.concretes.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthorizationController {

    private AuthenticationService authenticationService;

    @Autowired
    public AuthorizationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
    @PostMapping("/register")
    public UserResponse register(@RequestBody UserSaveRequest userSaveRequest){
        return authenticationService.register(userSaveRequest.getName(), userSaveRequest.getSurname(),
                userSaveRequest.getEmail(), userSaveRequest.getPassword());
    }
    @GetMapping("/confirm")
    @ResponseBody
    public String confirm(@RequestParam(name = "emailToken") String emailToken){
        return authenticationService.confirmationEmailToken(emailToken);
    }
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest){
        return authenticationService.login(loginRequest.email(), loginRequest.password());
    }
}
