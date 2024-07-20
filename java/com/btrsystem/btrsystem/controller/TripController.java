package com.btrsystem.btrsystem.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.btrsystem.btrsystem.models.Bus;
import com.btrsystem.btrsystem.models.Route;
import com.btrsystem.btrsystem.models.Trip;
import com.btrsystem.btrsystem.services.BusService;
import com.btrsystem.btrsystem.services.RouteService;
import com.btrsystem.btrsystem.services.TripService;

@RestController
@RequestMapping("/trip")
public class TripController {

	
	@Autowired
	private BusService busService;
	
	@Autowired
	private RouteService routeService;
	 
	@Autowired
	private TripService tripService;

	@PostMapping("/add")
	    public ResponseEntity<Trip> createTrip(@RequestBody Trip trip) {
	        Bus bus = busService.findByBusId(trip.getBus().getBusId()).orElse(null);
	        Route route = routeService.findByRouteId(trip.getRoute().getRouteId()).orElse(null);

	        if (bus == null || route == null) {
	            return ResponseEntity.badRequest().build();
	        }

	        trip.setTripId(UUID.randomUUID());
	        trip.setBus(bus);
	        trip.setRoute(route);

	        Trip savedTrip = tripService.save(trip);
	        return ResponseEntity.ok(savedTrip);
	    }
	
	
}
