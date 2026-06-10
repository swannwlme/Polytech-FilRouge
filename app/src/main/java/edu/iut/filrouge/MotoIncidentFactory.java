package edu.iut.filrouge;

public class MotoIncidentFactory implements IncidentFactory {

    @Override
    public Incident createIncident(String adresse, String description, double latitude, double longitude) {
        Incident incident = new MotoIncident(adresse, description, 0.0, 2.0f, latitude, longitude);
        incident.addObserver(EmergencyService.getInstance());
        return incident;
    }
}
