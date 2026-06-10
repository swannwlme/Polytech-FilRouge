package edu.iut.filrouge.model;

public interface IncidentFactory {
    double DEFAULT_LATITUDE = 43.6156;
    double DEFAULT_LONGITUDE = 7.0718;

    default Incident createIncident(String adresse, String description) {
        return createIncident(adresse, description, DEFAULT_LATITUDE, DEFAULT_LONGITUDE);
    }

    Incident createIncident(String adresse, String description, double latitude, double longitude);
}
