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
    private static final String ARG_INCIDENT_TITLE = "incidentTitle";
    private static final String DEFAULT_INCIDENT_TITLE = "Aucun incident sélectionné";

    private String incidentTitle = DEFAULT_INCIDENT_TITLE;

    public Screen1Fragment() {
    }

    public static Screen1Fragment newInstance(String incidentTitle) {
        Screen1Fragment fragment = new Screen1Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_INCIDENT_TITLE, incidentTitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            incidentTitle = getArguments().getString(ARG_INCIDENT_TITLE, DEFAULT_INCIDENT_TITLE);
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
        incidentTitleView.setText(incidentTitle);

        return view;
    }
}
