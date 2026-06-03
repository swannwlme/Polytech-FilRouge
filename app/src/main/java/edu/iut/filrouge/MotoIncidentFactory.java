package edu.iut.filrouge;

public class MotoIncidentFactory implements IncidentFactory {

    @Override
    public Incident createIncident(String adresse, String description) {
        Incident incident = new MotoIncident(adresse, description, 0.0, 2.0f);
        incident.addObserver(EmergencyService.getInstance());
        return incident;
    }
}
