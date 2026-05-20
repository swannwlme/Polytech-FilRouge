package edu.iut.filrouge;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.concurrent.ThreadLocalRandom;

public abstract class Incident implements Parcelable {

    private VehiculeType vehiculeType;
    private String adresse;
    private String description;
    private double distanceKm;
    private float status;

    protected Incident(VehiculeType vehiculeType, String adresse) {
        this(vehiculeType, adresse, 0.0, 0.0f, "");
    }

    protected Incident(VehiculeType vehiculeType, String adresse, double distanceKm) {
        this(vehiculeType, adresse, distanceKm, 0.0f, "");
    }

    protected Incident(VehiculeType vehiculeType, String adresse, double distanceKm, float status) {
        this(vehiculeType, adresse, distanceKm, status, "");
    }

    protected Incident(VehiculeType vehiculeType, String adresse, double distanceKm, float status,
                       String description) {
        this.vehiculeType = vehiculeType;
        this.adresse = adresse;
        this.distanceKm = distanceKm;
        this.status = status;
        this.description = description;
    }

    protected Incident(Parcel in) {
        String vehiculeTypeName = in.readString();
        vehiculeType = vehiculeTypeName == null ? null : VehiculeType.valueOf(vehiculeTypeName);
        adresse = in.readString();
        description = in.readString();
        distanceKm = in.readDouble();
        status = in.readFloat();
    }

    public VehiculeType getVehiculeType() {
        return vehiculeType;
    }

    public void setVehiculeType(VehiculeType vehiculeType) {
        this.vehiculeType = vehiculeType;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getDistance(String coord) {
        if (distanceKm > 0.0) {
            return distanceKm;
        }

        return ThreadLocalRandom.current().nextDouble(1.0, 20.0);
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public float getStatus() {
        return status;
    }

    public void setStatus(float status) {
        this.status = status;
    }

    public abstract String getSafetyProtocol();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(vehiculeType == null ? null : vehiculeType.name());
        dest.writeString(adresse);
        dest.writeString(description);
        dest.writeDouble(distanceKm);
        dest.writeFloat(status);
    }
}
