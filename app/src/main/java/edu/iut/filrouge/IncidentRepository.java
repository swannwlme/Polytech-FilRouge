package edu.iut.filrouge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class IncidentRepository {

    private static final IncidentFactory VOITURE_FACTORY = new VoitureIncidentFactory();
    private static final IncidentFactory MOTO_FACTORY = new MotoIncidentFactory();
    private static final IncidentFactory CAMION_FACTORY = new CamionIncidentFactory();

    private static final List<Incident> incidents = new ArrayList<>(Arrays.asList(
            createWithLocation(VOITURE_FACTORY, "Route des Colles", "Accident grave A7",
                    2.0f, 43.6156, 7.0718),
            createWithLocation(MOTO_FACTORY, "Rond-point Saint-Philippe", "Route barree",
                    4.0f, 43.6174, 7.0742),
            createWithLocation(CAMION_FACTORY, "Route des Lucioles", "Obstacle sur la chaussee",
                    15.3f, 43.6168, 7.0684),
            createWithLocation(VOITURE_FACTORY, "Bande d'arret d'urgence - A7 sortie 12", "Bouchon massif",
                    7.6f, 43.6141, 7.0735),
            createWithLocation(CAMION_FACTORY, "Voie lente - rocade Sud", "Travaux de nuit",
                    11.2f, 43.6137, 7.0701),
            createWithLocation(MOTO_FACTORY, "Accotement - D10", "Panne de signalisation",
                    5.4f, 43.6182, 7.0711),
            createWithLocation(VOITURE_FACTORY, "Bretelle d'acces centre-ville", "Vehicule a contresens",
                    3.8f, 43.6128, 7.0750),
            createWithLocation(CAMION_FACTORY, "Aire de repos des Pins", "Chargement renverse",
                    18.1f, 43.6192, 7.0675),
            createWithLocation(VOITURE_FACTORY, "Pont suspendu", "Priorite pompiers",
                    9.7f, 43.6119, 7.0694),
            createWithLocation(MOTO_FACTORY, "Rond-point Jean Jaures", "Depannage en cours",
                    6.2f, 43.6160, 7.0764)
    ));

    private IncidentRepository() {
    }

    public static List<Incident> getIncidents() {
        return incidents;
    }

    public static void addIncident(Incident incident) {
        incidents.add(0, incident);
    }

    private static Incident createWithLocation(IncidentFactory factory, String adresse, String description,
                                               double distanceKm, double latitude, double longitude) {
        Incident incident = factory.createIncident(adresse, description, latitude, longitude);
        incident.setDistanceKm(distanceKm);
        return incident;
    }
}
