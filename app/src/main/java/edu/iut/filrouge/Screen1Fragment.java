package edu.iut.filrouge;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Locale;

public class Screen1Fragment extends Fragment {

    public static final int FRAGMENT_ID = 0;
    private static final String ARG_INCIDENT = "incident";

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

        TextView incidentType = view.findViewById(R.id.incidentType);
        View incidentAddressRow = view.findViewById(R.id.incidentAddressRow);
        TextView incidentAddress = view.findViewById(R.id.incidentAddress);
        TextView incidentDistance = view.findViewById(R.id.incidentDistance);
        TextView incidentGps = view.findViewById(R.id.incidentGps);
        TextView ratingLabel = view.findViewById(R.id.ratingLabel);
        RatingBar incidentRating = view.findViewById(R.id.incidentRating);
        View descriptionHeader = view.findViewById(R.id.descriptionHeader);
        EditText incidentDescription = view.findViewById(R.id.incidentDescription);

        if (incident == null) {
            incidentType.setText("Aucun incident sélectionné");
            incidentAddressRow.setVisibility(View.GONE);
            incidentDistance.setVisibility(View.GONE);
            incidentGps.setVisibility(View.GONE);
            ratingLabel.setVisibility(View.GONE);
            incidentRating.setVisibility(View.GONE);
            descriptionHeader.setVisibility(View.GONE);
            incidentDescription.setVisibility(View.GONE);

            return view;
        }

        incidentType.setText("Type - " + incident.getVehiculeType().getVehiculeName());
        incidentAddress.setText(incident.getAdresse());
        incidentDistance.setText("Distance - " + formatDistance(incident.getDistanceKm()) + "km");
        incidentRating.setRating(incident.getStatus());
        incidentDescription.setText(incident.getDescription());

        return view;
    }

    private String formatDistance(double distanceKm) {
        if (distanceKm == Math.rint(distanceKm)) {
            return String.format(Locale.US, "%.0f", distanceKm);
        }

        return String.format(Locale.US, "%.1f", distanceKm);
    }
}
