package edu.iut.filrouge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class IncidentRepository {

    private static final List<Incident> incidents = new ArrayList<>(Arrays.asList(
            new Incident(VehiculeType.VOITURE, "13 rue du bonheur", 2.0f),
            new Incident(VehiculeType.MOTO, "41 avenue de la joie", 4.0f),
            new Incident(VehiculeType.CAMION, "2 impasse du cacatoès", 15.3f),
            new Incident(VehiculeType.VOITURE, "Bande d'arrêt d'urgence - A7 sortie 12", 7.6f),
            new Incident(VehiculeType.CAMION, "Voie lente - rocade Sud", 11.2f),
            new Incident(VehiculeType.MOTO, "Accotement - D10", 5.4f),
            new Incident(VehiculeType.VOITURE, "Bretelle d'accès centre-ville", 3.8f),
            new Incident(VehiculeType.CAMION, "Aire de repos des Pins", 18.1f),
            new Incident(VehiculeType.VOITURE, "Pont suspendu", 9.7f),
            new Incident(VehiculeType.MOTO, "Rond-point Jean Jaurès", 6.2f)
    ));

    private IncidentRepository() {
    }

    public static List<Incident> getIncidents() {
        return incidents;
    }

    public static void addIncident(Incident incident) {
        incidents.add(0, incident);
    }
}
