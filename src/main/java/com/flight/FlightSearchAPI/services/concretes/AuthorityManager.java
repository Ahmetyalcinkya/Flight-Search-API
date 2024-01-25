package com.flight.FlightSearchAPI.services.concretes;

import com.flight.FlightSearchAPI.dto.responses.AuthorityResponse;
import com.flight.FlightSearchAPI.entities.Authority;
import com.flight.FlightSearchAPI.repositories.AuthorityRepository;
import com.flight.FlightSearchAPI.services.abstracts.AuthorityService;
import com.flight.FlightSearchAPI.services.abstracts.ModelMapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorityManager implements AuthorityService {

    private AuthorityRepository authorityRepository;
    private ModelMapperService modelMapperService;

    @Autowired
    public AuthorityManager(AuthorityRepository authorityRepository, ModelMapperService modelMapperService) {
        this.authorityRepository = authorityRepository;
        this.modelMapperService = modelMapperService;
    }

    @Override
    public AuthorityResponse getAuthority() {
        Authority authority = authorityRepository.findByAuthority("USER");
        return modelMapperService.forResponse().map(authority, AuthorityResponse.class);
    }
}
