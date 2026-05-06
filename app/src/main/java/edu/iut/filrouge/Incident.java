package edu.iut.filrouge;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.concurrent.ThreadLocalRandom;

public class Incident implements Parcelable {

    private VehiculeType vehiculeType;
    private String adresse;

    public Incident(VehiculeType vehiculeType, String adresse) {
        this.vehiculeType = vehiculeType;
        this.adresse = adresse;
    }

    protected Incident(Parcel in) {
        String vehiculeTypeName = in.readString();
        vehiculeType = vehiculeTypeName == null ? null : VehiculeType.valueOf(vehiculeTypeName);
        adresse = in.readString();
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
        return ThreadLocalRandom.current().nextDouble(1.0, 20.0);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(vehiculeType == null ? null : vehiculeType.name());
        dest.writeString(adresse);
    }
}
