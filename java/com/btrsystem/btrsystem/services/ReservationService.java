package com.btrsystem.btrsystem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.btrsystem.btrsystem.models.Reservation;
import com.btrsystem.btrsystem.repository.ReservationRepository;

@Service
public class ReservationService {

	
	@Autowired
	private ReservationRepository reservationRepository;
	
	public Reservation save(Reservation reservation) {
		return reservationRepository.save(reservation);
	}

	  public Reservation makeReservation(Reservation reservation) {
	        // Additional business logic (e.g., checking available seats) can be added here
	        return reservationRepository.save(reservation);
	    }
}
