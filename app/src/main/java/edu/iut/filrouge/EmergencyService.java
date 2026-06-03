package edu.iut.filrouge;

import android.util.Log;

public final class EmergencyService implements IssueObserver {

    private static final String TAG = "EmergencyService";
    private static final EmergencyService INSTANCE = new EmergencyService();

    private EmergencyService() {
    }

    public static EmergencyService getInstance() {
        return INSTANCE;
    }

    @Override
    public void onStatusChanged(Incident incident) {
        Log.d(TAG, "Changement de statut : " + getIncidentLabel(incident)
                + " - statut " + incident.getStatus());
    }

    @Override
    public void onPriorityChanged(Incident incident) {
        Log.d(TAG, "Changement de priorité : " + getIncidentLabel(incident));
    }

    private String getIncidentLabel(Incident incident) {
        if (incident.getVehiculeType() == null) {
            return incident.getAdresse();
        }

        return incident.getVehiculeType().getVehiculeName() + " - " + incident.getAdresse();
    }
}
