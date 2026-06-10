package edu.iut.filrouge;

public class CamionIncidentFactory implements IncidentFactory {

    @Override
    public Incident createIncident(String adresse, String description, double latitude, double longitude) {
        Incident incident = new CamionIncident(adresse, description, 0.0, 3.0f, latitude, longitude);
        incident.addObserver(EmergencyService.getInstance());
        return incident;
    }
}
