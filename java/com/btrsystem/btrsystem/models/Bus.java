package com.btrsystem.btrsystem.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name = "bus")
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID busId;

    @NotBlank
    private String busNumber;

    @NotBlank
    private String model;

    @NotNull
    private Integer capacity;

    @NotBlank
    private String operatorName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    // Default constructor
    public Bus() {}

    // Parameterized constructor
    public Bus(UUID busId, @NotBlank String busNumber, @NotBlank String model, @NotNull Integer capacity, @NotBlank String operatorName) {
        this.busId = busId;
        this.busNumber = busNumber;
        this.model = model;
        this.capacity = capacity;
        this.operatorName = operatorName;
    }

    // Getters and Setters
    public UUID getBusId() {
        return busId;
    }

    public void setBusId(UUID busId) {
        this.busId = busId;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
