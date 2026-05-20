package edu.iut.filrouge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class IncidentRepository {

    private static final IncidentFactory VOITURE_FACTORY = new VoitureIncidentFactory();
    private static final IncidentFactory MOTO_FACTORY = new MotoIncidentFactory();
    private static final IncidentFactory CAMION_FACTORY = new CamionIncidentFactory();

    private static final List<Incident> incidents = new ArrayList<>(Arrays.asList(
            createWithDistance(VOITURE_FACTORY, "13 rue du bonheur", 2.0f),
            createWithDistance(MOTO_FACTORY, "41 avenue de la joie", 4.0f),
            createWithDistance(CAMION_FACTORY, "2 impasse du cacatoès", 15.3f),
            createWithDistance(VOITURE_FACTORY, "Bande d'arrêt d'urgence - A7 sortie 12", 7.6f),
            createWithDistance(CAMION_FACTORY, "Voie lente - rocade Sud", 11.2f),
            createWithDistance(MOTO_FACTORY, "Accotement - D10", 5.4f),
            createWithDistance(VOITURE_FACTORY, "Bretelle d'accès centre-ville", 3.8f),
            createWithDistance(CAMION_FACTORY, "Aire de repos des Pins", 18.1f),
            createWithDistance(VOITURE_FACTORY, "Pont suspendu", 9.7f),
            createWithDistance(MOTO_FACTORY, "Rond-point Jean Jaurès", 6.2f)
    ));

    private IncidentRepository() {
    }

    public static List<Incident> getIncidents() {
        return incidents;
    }

    public static void addIncident(Incident incident) {
        incidents.add(0, incident);
    }

    private static Incident createWithDistance(IncidentFactory factory, String adresse, double distanceKm) {
        Incident incident = factory.createIncident(adresse, "");
        incident.setDistanceKm(distanceKm);
        return incident;
    }
}
