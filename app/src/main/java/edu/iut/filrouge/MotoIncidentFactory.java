package edu.iut.filrouge;

public class MotoIncidentFactory implements IncidentFactory {

    @Override
    public Incident createIncident(String adresse, String description) {
        return new MotoIncident(adresse, description, 0.0, 2.0f);
    }
}
