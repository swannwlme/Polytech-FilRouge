package edu.iut.filrouge.view;

import android.content.Context;
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

import edu.iut.filrouge.R;
import edu.iut.filrouge.controller.Notifiable;
import edu.iut.filrouge.model.Incident;
import edu.iut.filrouge.model.IssueObserver;

public class Screen1Fragment extends Fragment implements IssueObserver {

    public static final int FRAGMENT_ID = 0;
    public static final int ACTION_INCIDENT_DISPLAYED = 1;
    private static final String ARG_INCIDENT = "incident";

    private Incident incident;
    private Notifiable notifiable;

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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof Notifiable) {
            notifiable = (Notifiable) context;
        } else {
            throw new IllegalStateException(context + " doit implémenter Notifiable");
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
        View incidentPhotoSection = view.findViewById(R.id.incidentPhotoSection);

        if (incident == null) {
            incidentType.setText("Aucun incident sélectionné");
            incidentAddressRow.setVisibility(View.GONE);
            incidentDistance.setVisibility(View.GONE);
            incidentGps.setVisibility(View.GONE);
            ratingLabel.setVisibility(View.GONE);
            incidentRating.setVisibility(View.GONE);
            descriptionHeader.setVisibility(View.GONE);
            incidentDescription.setVisibility(View.GONE);
            incidentPhotoSection.setVisibility(View.GONE);

            return view;
        }

        incidentType.setText("Type - " + incident.getVehiculeType().getVehiculeName());
        incidentAddress.setText(incident.getAdresse());
        incidentDistance.setText("Distance - " + formatDistance(incident.getDistanceKm()) + "km");
        incidentGps.setText("GPS : " + formatCoordinates(incident));
        incidentRating.setRating(incident.getStatus());
        incidentDescription.setText(incident.getDescription());
        setupCameraFragment();
        displayIncidentPicture();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (incident != null) {
            incident.addObserver(this);
            displayIncidentPicture();

            if (notifiable != null) {
                notifiable.onDataChange(FRAGMENT_ID, incident, ACTION_INCIDENT_DISPLAYED, null);
            }
        }
    }

    @Override
    public void onStop() {
        if (incident != null) {
            incident.removeObserver(this);
        }

        super.onStop();
    }

    @Override
    public void onDetach() {
        notifiable = null;
        super.onDetach();
    }

    @Override
    public void onStatusChanged(Incident incident) {
    }

    @Override
    public void onPriorityChanged(Incident incident) {
    }

    @Override
    public void onPictureChanged(Incident incident) {
        displayIncidentPicture();
    }

    private void setupCameraFragment() {
        if (getChildFragmentManager().findFragmentById(R.id.cameraFragmentContainer) != null) {
            return;
        }

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.cameraFragmentContainer, new CameraFragment())
                .commit();
    }

    private void displayIncidentPicture() {
        Bundle result = new Bundle();
        result.putString(CameraFragment.BUNDLE_PICTURE_PATH, incident == null ? null : incident.getPicture());
        getChildFragmentManager().setFragmentResult(CameraFragment.REQUEST_DISPLAY_PICTURE, result);
    }

    private String formatDistance(double distanceKm) {
        if (distanceKm == Math.rint(distanceKm)) {
            return String.format(Locale.US, "%.0f", distanceKm);
        }

        return String.format(Locale.US, "%.1f", distanceKm);
    }

    private String formatCoordinates(Incident incident) {
        return String.format(Locale.US, "%.4f, %.4f", incident.getLatitude(), incident.getLongitude());
    }
}
