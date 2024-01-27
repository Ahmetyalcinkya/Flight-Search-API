package com.flight.FlightSearchAPI.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FlightException extends RuntimeException {
    private HttpStatus status;

    public FlightException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
