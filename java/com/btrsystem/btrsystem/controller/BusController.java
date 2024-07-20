package com.btrsystem.btrsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.btrsystem.btrsystem.models.Bus;
import com.btrsystem.btrsystem.models.User;

import com.btrsystem.btrsystem.services.BusService;
import com.btrsystem.btrsystem.services.UsersService;

@RestController
@RequestMapping("/bus")
public class BusController {
	@Autowired
	private UsersService usersService;
	@Autowired
	private BusService busService;
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")

    @PostMapping("/bus")
    public ResponseEntity<Bus> addBus(@RequestBody Bus bus, @RequestParam int userId) {
        // Retrieve the user by userId
        User user = usersService.getUserById(userId);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        // Set the user to the bus
        bus.setUser(user);

        // Save the bus
        Bus savedBus = busService.saveBus(bus);

        return ResponseEntity.ok(savedBus);
    }

}
