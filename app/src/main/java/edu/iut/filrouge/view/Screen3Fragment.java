package edu.iut.filrouge.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Locale;

import edu.iut.filrouge.R;
import edu.iut.filrouge.controller.Notifiable;
import edu.iut.filrouge.model.CamionIncidentFactory;
import edu.iut.filrouge.model.Incident;
import edu.iut.filrouge.model.IncidentFactory;
import edu.iut.filrouge.model.IssueManager;
import edu.iut.filrouge.model.MotoIncidentFactory;
import edu.iut.filrouge.model.VehiculeType;
import edu.iut.filrouge.model.VoitureIncidentFactory;

public class Screen3Fragment extends Fragment {

    private static final String TAG = "Screen3Fragment";

    public static final int FRAGMENT_ID = 2;
    public static final int ACTION_INCIDENT_REPORTED = 0;

    private Notifiable notifiable;
    private IssueManager issueManager;
    private VehiculeType selectedVehicleType = VehiculeType.VOITURE;

    private ImageButton motoButton;
    private ImageButton carButton;
    private ImageButton truckButton;
    private EditText descriptionInput;
    private EditText currentTargetEditText;

    private final ActivityResultLauncher<Intent> voiceLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    ArrayList<String> matches = result.getData()
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    if (matches != null && !matches.isEmpty() && currentTargetEditText != null) {
                        currentTargetEditText.setText(matches.get(0));
                        currentTargetEditText.setSelection(currentTargetEditText.getText().length());
                    }
                }
            }
    );

    public Screen3Fragment() {
    }

    public void setIssueManager(IssueManager issueManager) {
        this.issueManager = issueManager;
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

        view.findViewById(R.id.descriptionVoiceButton)
                .setOnClickListener(v -> startVoiceRecognition(descriptionInput));
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
        if (issueManager != null) {
            issueManager.addIncident(incident);
        }

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

    private void startVoiceRecognition(EditText target) {
        currentTargetEditText = target;

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Parlez pour remplir le champ...");

        try {
            voiceLauncher.launch(intent);
        } catch (Exception e) {
            Log.e(TAG, "Reconnaissance vocale non supportee sur cet appareil.", e);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        notifiable = null;
    }
}
