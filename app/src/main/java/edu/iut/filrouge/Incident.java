package edu.iut.filrouge;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.concurrent.ThreadLocalRandom;

public class Incident implements Parcelable {

    private VehiculeType vehiculeType;
    private String adresse;
    private double distanceKm;
    private float status;

    public Incident(VehiculeType vehiculeType, String adresse) {
        this(vehiculeType, adresse, 0.0, 0.0f);
    }

    public Incident(VehiculeType vehiculeType, String adresse, double distanceKm) {
        this(vehiculeType, adresse, distanceKm, 0.0f);
    }

    public Incident(VehiculeType vehiculeType, String adresse, double distanceKm, float status) {
        this.vehiculeType = vehiculeType;
        this.adresse = adresse;
        this.distanceKm = distanceKm;
        this.status = status;
    }

    protected Incident(Parcel in) {
        String vehiculeTypeName = in.readString();
        vehiculeType = vehiculeTypeName == null ? null : VehiculeType.valueOf(vehiculeTypeName);
        adresse = in.readString();
        distanceKm = in.readDouble();
        status = in.readFloat();
    }

    public static final Creator<Incident> CREATOR = new Creator<Incident>() {
        @Override
        public Incident createFromParcel(Parcel in) {
            return new Incident(in);
        }

        @Override
        public Incident[] newArray(int size) {
            return new Incident[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(vehiculeType == null ? null : vehiculeType.name());
        dest.writeString(adresse);
        dest.writeDouble(distanceKm);
        dest.writeFloat(status);
    }
}
