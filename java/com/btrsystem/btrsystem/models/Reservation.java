package com.btrsystem.btrsystem.models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID reservationId;

    @ManyToOne
    @NotNull
    private User user;

    @ManyToOne
    @NotNull
    private Trip trip;

    @NotNull
    private Integer numberOfSeats;

    @NotNull
    private Float totalAmount;

    // Default constructor
    public Reservation() {}

    // Parameterized constructor
    public Reservation(UUID reservationId, @NotNull User user, @NotNull Trip trip, @NotNull Integer numberOfSeats, @NotNull Float totalAmount) {
        this.reservationId = reservationId;
        this.user = user;
        this.trip = trip;
        this.numberOfSeats = numberOfSeats;
        this.totalAmount = totalAmount;
    }

    // Getters and Setters
    public UUID getReservationId() {
        return reservationId;
    }

    public void setReservationId(UUID reservationId) {
        this.reservationId = reservationId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
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
