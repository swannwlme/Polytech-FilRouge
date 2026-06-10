package edu.iut.filrouge;

public class VoitureIncidentFactory implements IncidentFactory {

    @Override
    public Incident createIncident(String adresse, String description, double latitude, double longitude) {
        Incident incident = new VoitureIncident(adresse, description, 0.0, 1.0f, latitude, longitude);
        incident.addObserver(EmergencyService.getInstance());
        return incident;
    }
}
