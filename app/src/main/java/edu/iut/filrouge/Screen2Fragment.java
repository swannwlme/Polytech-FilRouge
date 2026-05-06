package edu.iut.filrouge;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Screen2Fragment extends Fragment {

    public static final int FRAGMENT_ID = 1;

    private Notifiable notifiable;

    private final List<Incident> incidents = Arrays.asList(
            new Incident(VehiculeType.VOITURE, "Bande d'arrêt d'urgence - A7 sortie 12"),
            new Incident(VehiculeType.CAMION, "Voie lente - rocade Sud"),
            new Incident(VehiculeType.MOTO, "Accotement - D10"),
            new Incident(VehiculeType.VOITURE, "Bretelle d'accès centre-ville"),
            new Incident(VehiculeType.CAMION, "Aire de repos des Pins"),
            new Incident(VehiculeType.VOITURE, "Pont suspendu"),
            new Incident(VehiculeType.MOTO, "Rond-point Jean Jaurès"),
            new Incident(VehiculeType.CAMION, "Départementale D22"),
            new Incident(VehiculeType.VOITURE, "Tunnel Nord"),
            new Incident(VehiculeType.MOTO, "Route forestière")
    );

    public Screen2Fragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof Notifiable) {
            notifiable = (Notifiable) context;
        } else {
            throw new RuntimeException(context + " doit implémenter Notifiable");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_screen2, container, false);

        ListView incidentList = view.findViewById(R.id.list_item);
        List<String> incidentNames = new ArrayList<>();

        for (Incident incident : incidents) {
            incidentNames.add(getIncidentName(incident));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                incidentNames
        );

        incidentList.setAdapter(adapter);
        incidentList.setOnItemClickListener((parent, itemView, position, id) ->
                notifiable.onDataChange(FRAGMENT_ID, incidentNames.get(position))
        );

        return view;
    }

    private String getIncidentName(Incident incident) {
        return incident.getVehiculeType().getVehiculeName() + " arrêté - " + incident.getAdresse();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        notifiable = null;
    }
}
