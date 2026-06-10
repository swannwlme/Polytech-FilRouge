package edu.iut.filrouge;

import android.os.Parcel;
import android.os.Parcelable;

public class MotoIncident extends Incident {

    public MotoIncident(String adresse, String description, double distanceKm, float status) {
        super(VehiculeType.MOTO, adresse, distanceKm, status, description);
    }

    public MotoIncident(String adresse, String description, double distanceKm, float status,
                        double latitude, double longitude) {
        super(VehiculeType.MOTO, adresse, distanceKm, status, description, latitude, longitude);
    }

    protected MotoIncident(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<MotoIncident> CREATOR =
            new Parcelable.Creator<MotoIncident>() {
                @Override
                public MotoIncident createFromParcel(Parcel in) {
                    return new MotoIncident(in);
                }

                @Override
                public MotoIncident[] newArray(int size) {
                    return new MotoIncident[size];
                }
            };

    @Override
    public String getSafetyProtocol() {
        return "Proteger le conducteur et baliser rapidement la zone.";
    }
}
