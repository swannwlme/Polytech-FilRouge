package edu.iut.filrouge;

public class CamionIncidentFactory implements IncidentFactory {

    @Override
    public Incident createIncident(String adresse, String description) {
        return new CamionIncident(adresse, description, 0.0, 3.0f);
    }
}
