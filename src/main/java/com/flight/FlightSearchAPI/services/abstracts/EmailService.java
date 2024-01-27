package com.flight.FlightSearchAPI.services.abstracts;

public interface EmailService {
    String sendEmail(String to, String subject, String body);
}
