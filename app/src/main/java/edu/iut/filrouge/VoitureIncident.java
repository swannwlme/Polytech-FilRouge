package edu.iut.filrouge;

import android.os.Parcel;
import android.os.Parcelable;

public class VoitureIncident extends Incident {

    public VoitureIncident(String adresse, String description, double distanceKm, float status) {
        super(VehiculeType.VOITURE, adresse, distanceKm, status, description);
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
