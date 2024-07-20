package com.btrsystem.btrsystem.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.btrsystem.btrsystem.models.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID>{

}
