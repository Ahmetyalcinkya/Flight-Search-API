package com.flight.FlightSearchAPI.controllers;

import com.flight.FlightSearchAPI.dto.responses.AuthorityResponse;
import com.flight.FlightSearchAPI.services.abstracts.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authority")
public class AuthorityController {
    private AuthorityService authorityService;

    @Autowired
    public AuthorityController(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    @GetMapping
    public AuthorityResponse getAuthority(){
        return authorityService.getAuthority();
    }
}
