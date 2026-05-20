package edu.iut.filrouge;

import android.os.Parcel;
import android.os.Parcelable;

public class CamionIncident extends Incident {

    public CamionIncident(String adresse, String description, double distanceKm, float status) {
        super(VehiculeType.CAMION, adresse, distanceKm, status, description);
    }

    protected CamionIncident(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<CamionIncident> CREATOR =
            new Parcelable.Creator<CamionIncident>() {
                @Override
                public CamionIncident createFromParcel(Parcel in) {
                    return new CamionIncident(in);
                }

                @Override
                public CamionIncident[] newArray(int size) {
                    return new CamionIncident[size];
                }
            };

    @Override
    public String getSafetyProtocol() {
        return "Garder une grande distance et ne pas rester dans l'angle mort.";
    }
}
