package edu.iut.filrouge;

public class VoitureIncidentFactory implements IncidentFactory {

    @Override
    public Incident createIncident(String adresse, String description) {
        return new VoitureIncident(adresse, description, 0.0, 1.0f);
    }
}
