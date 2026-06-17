package edu.iut.filrouge.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IssueManager implements ModelObservable {

    private final IncidentFactory voitureFactory = new VoitureIncidentFactory();
    private final IncidentFactory motoFactory = new MotoIncidentFactory();
    private final IncidentFactory camionFactory = new CamionIncidentFactory();

    private final List<Incident> incidents = new ArrayList<>();
    private final List<ViewObserver> observers = new ArrayList<>();

    public void populate() {
        if (!incidents.isEmpty()) {
            return;
        }

        incidents.add(createWithLocation(voitureFactory, "Route des Colles", "Accident grave A7",
                2.0f, 43.6156, 7.0718));
        incidents.add(createWithLocation(motoFactory, "Rond-point Saint-Philippe", "Route barree",
                4.0f, 43.6174, 7.0742));
        incidents.add(createWithLocation(camionFactory, "Route des Lucioles", "Obstacle sur la chaussee",
                15.3f, 43.6168, 7.0684));
        incidents.add(createWithLocation(voitureFactory, "Bande d'arret d'urgence - A7 sortie 12", "Bouchon massif",
                7.6f, 43.6141, 7.0735));
        incidents.add(createWithLocation(camionFactory, "Voie lente - rocade Sud", "Travaux de nuit",
                11.2f, 43.6137, 7.0701));
        incidents.add(createWithLocation(motoFactory, "Accotement - D10", "Panne de signalisation",
                5.4f, 43.6182, 7.0711));
        incidents.add(createWithLocation(voitureFactory, "Bretelle d'acces centre-ville", "Vehicule a contresens",
                3.8f, 43.6128, 7.0750));
        incidents.add(createWithLocation(camionFactory, "Aire de repos des Pins", "Chargement renverse",
                18.1f, 43.6192, 7.0675));
        incidents.add(createWithLocation(voitureFactory, "Pont suspendu", "Priorite pompiers",
                9.7f, 43.6119, 7.0694));
        incidents.add(createWithLocation(motoFactory, "Rond-point Jean Jaures", "Depannage en cours",
                6.2f, 43.6160, 7.0764));
    }

    public List<Incident> getIncidents() {
        return Collections.unmodifiableList(incidents);
    }

    public void addIncident(Incident incident) {
        incidents.add(0, incident);
        notifyObservers();
    }

    public void setLocation(Incident incident, double latitude, double longitude) {
        if (incident == null) {
            return;
        }

        incident.setLatitude(latitude);
        incident.setLongitude(longitude);
        notifyObservers();
    }

    public void setPicture(Incident incident, String picture) {
        if (incident == null) {
            return;
        }

        incident.setPicture(picture);
        notifyObservers();
    }

    private Incident createWithLocation(IncidentFactory factory, String adresse, String description,
                                        double distanceKm, double latitude, double longitude) {
        Incident incident = factory.createIncident(adresse, description, latitude, longitude);
        incident.setDistanceKm(distanceKm);
        return incident;
    }

    @Override
    public void addObserver(ViewObserver observer) {
        if (observer == null || observers.contains(observer)) {
            return;
        }

        observers.add(observer);
    }

    @Override
    public void removeObserver(ViewObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (ViewObserver observer : new ArrayList<>(observers)) {
            observer.onModelChanged();
        }
    }
}
