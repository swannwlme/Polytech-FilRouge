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

import java.util.List;

public class Screen2Fragment extends Fragment implements ClickableIncident<Incident> {

    public static final int FRAGMENT_ID = 1;
    public static final int ACTION_OPEN_DETAILS = 1;
    public static final int ACTION_STATUS_CHANGED = 2;

    private Notifiable notifiable;
    private List<Incident> incidents;
    private IncidentAdapter adapter;

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

        incidents = IncidentRepository.getIncidents();
        ListView incidentList = view.findViewById(R.id.list_item);
        adapter = new IncidentAdapter(requireContext(), incidents, this);
        incidentList.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
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
