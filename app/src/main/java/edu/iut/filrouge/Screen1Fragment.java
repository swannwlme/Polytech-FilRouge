package edu.iut.filrouge;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Screen1Fragment extends Fragment {

    public static final int FRAGMENT_ID = 0;
    private static final String ARG_INCIDENT = "incident";
    private static final String DEFAULT_INCIDENT_DETAILS = "Aucun incident sélectionné";

    private Incident incident;

    public Screen1Fragment() {
    }

    public static Screen1Fragment newInstance(Incident incident) {
        Screen1Fragment fragment = new Screen1Fragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_INCIDENT, incident);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            incident = getArguments().getParcelable(ARG_INCIDENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_screen1, container, false);

        TextView title = view.findViewById(R.id.title);
        TextView incidentTitleView = view.findViewById(R.id.incidentTitle);

        title.setText("Détail de l'incident");
        incidentTitleView.setText(getIncidentDetails());

        return view;
    }

    private String getIncidentDetails() {
        if (incident == null) {
            return DEFAULT_INCIDENT_DETAILS;
        }

        return incident.getVehiculeType().getVehiculeName()
                + " - " + incident.getDistanceKm() + " km\n"
                + incident.getAdresse()
                + "\nStatut : " + incident.getStatus() + "/5";
    }
}
