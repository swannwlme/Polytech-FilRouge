package edu.iut.filrouge;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Screen3Fragment extends Fragment {

    public static final int FRAGMENT_ID = 2;
    public static final int ACTION_INCIDENT_REPORTED = 0;

    private Notifiable notifiable;
    private VehiculeType selectedVehicleType = VehiculeType.VOITURE;

    private ImageButton motoButton;
    private ImageButton carButton;
    private ImageButton truckButton;
    private EditText descriptionInput;

    public Screen3Fragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof Notifiable) {
            notifiable = (Notifiable) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_screen3, container, false);

        motoButton = view.findViewById(R.id.motoButton);
        carButton = view.findViewById(R.id.carButton);
        truckButton = view.findViewById(R.id.truckButton);
        descriptionInput = view.findViewById(R.id.incidentDescriptionInput);

        setupVehicleButton(motoButton, VehiculeType.MOTO);
        setupVehicleButton(carButton, VehiculeType.VOITURE);
        setupVehicleButton(truckButton, VehiculeType.CAMION);
        selectVehicle(VehiculeType.VOITURE);

        view.findViewById(R.id.reportIncidentButton).setOnClickListener(v -> reportIncident());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (notifiable != null) {
            notifiable.onFragmentDisplayed(FRAGMENT_ID);
        }
    }

    private void setupVehicleButton(ImageButton button, VehiculeType vehicleType) {
        button.setOnClickListener(v -> selectVehicle(vehicleType));
    }

    private void selectVehicle(VehiculeType vehicleType) {
        selectedVehicleType = vehicleType;

        updateVehicleButton(motoButton, vehicleType == VehiculeType.MOTO);
        updateVehicleButton(carButton, vehicleType == VehiculeType.VOITURE);
        updateVehicleButton(truckButton, vehicleType == VehiculeType.CAMION);
    }

    private void updateVehicleButton(ImageButton button, boolean selected) {
        int background = selected
                ? R.drawable.incident_vehicle_option_selected_background
                : R.drawable.incident_vehicle_option_background;
        button.setBackgroundResource(background);
        button.setSelected(selected);
    }

    private void reportIncident() {
        String description = descriptionInput.getText().toString().trim();

        if (description.isEmpty()) {
            descriptionInput.setError(getString(R.string.incident_description_required));
            return;
        }

        IncidentFactory factory = getFactoryFor(selectedVehicleType);
        Incident incident = factory.createIncident(
                getString(R.string.incident_default_location),
                description
        );
        IncidentRepository.addIncident(incident);

        if (notifiable != null) {
            notifiable.onDataChange(
                    FRAGMENT_ID,
                    incident,
                    ACTION_INCIDENT_REPORTED,
                    incident.getSafetyProtocol()
            );
        }

        Toast.makeText(
                requireContext(),
                getString(R.string.incident_report_sent) + " : " + selectedVehicleType.getVehiculeName(),
                Toast.LENGTH_SHORT
        ).show();
    }

    private IncidentFactory getFactoryFor(VehiculeType vehicleType) {
        switch (vehicleType) {
            case MOTO:
                return new MotoIncidentFactory();
            case CAMION:
                return new CamionIncidentFactory();
            case VOITURE:
            default:
                return new VoitureIncidentFactory();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        notifiable = null;
    }
}
