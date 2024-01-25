package com.flight.FlightSearchAPI.repositories;

import com.flight.FlightSearchAPI.entities.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Transactional(readOnly = true)
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

    @Query("SELECT c FROM ConfirmationToken c WHERE c.emailToken = :emailToken")
    Optional<ConfirmationToken> findByConfirmationToken(String emailToken);

    @Transactional
    @Modifying
    @Query("UPDATE ConfirmationToken c SET c.confirmedAt = ?2 WHERE c.emailToken = ?1")
    void updateConfirmedAt(String emailToken, LocalDateTime confirmedAt);
}
