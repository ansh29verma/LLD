package com.ansh.VehicleFactory;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class Vehicle {
    private String registrationNumber;
    private String model;
    private VehicleType type;
    private VehicleStatus status;
    private double baseRentalPrice;

    public Vehicle(String registrationNumber, String model, VehicleType type,
                  double baseRentalPrice) {
        this.registrationNumber = registrationNumber;
        this.model = model;
        this.type = type;
        this.status = VehicleStatus.AVAILABLE;
        this.baseRentalPrice = baseRentalPrice;
    }


    public abstract double calculateRentalPrice(int days);
}
