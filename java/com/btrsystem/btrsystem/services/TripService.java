package com.btrsystem.btrsystem.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.btrsystem.btrsystem.models.Trip;
import com.btrsystem.btrsystem.repository.TripRepository;

@Service
public class TripService {
	
	@Autowired
	private TripRepository tripRepository;
	
	   public Optional<Trip> findTripsByBusId(UUID tripId) {
	        return tripRepository.findById(tripId);
	    }
	   
	   public Trip save(Trip trip)
	   {
		   return tripRepository.save(trip);
	   }

	
}
