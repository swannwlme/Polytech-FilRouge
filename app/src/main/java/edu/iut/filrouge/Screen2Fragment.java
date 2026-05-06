package edu.iut.filrouge;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Arrays;
import java.util.List;

public class Screen2Fragment extends Fragment implements ClickableIncident<Incident> {

    public static final int FRAGMENT_ID = 1;
    public static final int ACTION_OPEN_DETAILS = 1;
    public static final int ACTION_STATUS_CHANGED = 2;

    private Notifiable notifiable;

    private final List<Incident> incidents = Arrays.asList(
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
        IncidentAdapter adapter = new IncidentAdapter(requireContext(), incidents, this);
        incidentList.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        notifiable.onFragmentDisplayed(FRAGMENT_ID);
    }

    @Override
    public void onRatingBarChange(int itemIndex, float value, IncidentAdapter adapter, List<Incident> items) {
        Incident incident = items.get(itemIndex);
        incident.setStatus(value);
        adapter.notifyDataSetChanged();
        notifiable.onDataChange(FRAGMENT_ID, incident, ACTION_STATUS_CHANGED, value);
    }

    @Override
    public void onClickItem(List<Incident> items, int itemIndex) {
        notifiable.onDataChange(FRAGMENT_ID, items.get(itemIndex), ACTION_OPEN_DETAILS, itemIndex);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        notifiable = null;
    }
}
