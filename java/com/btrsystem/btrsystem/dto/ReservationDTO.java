package com.btrsystem.btrsystem.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class ReservationDTO {

    @NotNull
    private UUID tripId;

    @NotNull
    private int userId;  // Change this to int

    @NotNull
    private Integer numberOfSeats;

    @NotNull
    private Float totalAmount;

    // Getters and Setters
    public UUID getTripId() {
        return tripId;
    }

    public void setTripId(UUID tripId) {
        this.tripId = tripId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(Integer numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public Float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Float totalAmount) {
        this.totalAmount = totalAmount;
    }
}
