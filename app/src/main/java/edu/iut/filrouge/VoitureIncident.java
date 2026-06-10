package edu.iut.filrouge;

import android.os.Parcel;
import android.os.Parcelable;

public class VoitureIncident extends Incident {

    public VoitureIncident(String adresse, String description, double distanceKm, float status) {
        super(VehiculeType.VOITURE, adresse, distanceKm, status, description);
    }

    public VoitureIncident(String adresse, String description, double distanceKm, float status,
                           double latitude, double longitude) {
        super(VehiculeType.VOITURE, adresse, distanceKm, status, description, latitude, longitude);
    }

    protected VoitureIncident(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<VoitureIncident> CREATOR =
            new Parcelable.Creator<VoitureIncident>() {
                @Override
                public VoitureIncident createFromParcel(Parcel in) {
                    return new VoitureIncident(in);
                }

                @Override
                public VoitureIncident[] newArray(int size) {
                    return new VoitureIncident[size];
                }
            };

    @Override
    public String getSafetyProtocol() {
        return "Allumer les feux de detresse et rester a distance du vehicule.";
    }
}
