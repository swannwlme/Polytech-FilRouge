package edu.iut.filrouge.model;

public enum VehiculeType {
    VOITURE("Voiture", "ic_vehicle_car"),
    MOTO("Moto", "ic_vehicle_moto"),
    CAMION("Camion", "ic_vehicle_truck");

    private final String vehiculeName;
    private final String image;

    VehiculeType(String vehiculeName, String image) {
        this.vehiculeName = vehiculeName;
        this.image = image;
    }

    public String getVehiculeName() {
        return vehiculeName;
    }

    public String getImage() {
        return image;
    }
}
