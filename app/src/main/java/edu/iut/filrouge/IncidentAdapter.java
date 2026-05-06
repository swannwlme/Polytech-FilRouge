package edu.iut.filrouge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Locale;

public class IncidentAdapter extends ArrayAdapter<Incident> {

    private final List<Incident> incidents;
    private final ClickableIncident<Incident> clickableIncident;
    private final LayoutInflater inflater;

    public IncidentAdapter(@NonNull Context context, @NonNull List<Incident> incidents,
                           @NonNull ClickableIncident<Incident> clickableIncident) {
        super(context, 0, incidents);
        this.incidents = incidents;
        this.clickableIncident = clickableIncident;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_incident, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Incident incident = incidents.get(position);
        holder.icon.setImageResource(getVehicleIconResource(incident));
        holder.title.setText(getIncidentTitle(incident));
        holder.address.setText(incident.getAdresse());

        holder.status.setOnRatingBarChangeListener(null);
        holder.status.setRating(incident.getStatus());
        holder.status.setOnRatingBarChangeListener((ratingBar, value, fromUser) -> {
            if (fromUser) {
                incident.setStatus(value);
                clickableIncident.onRatingBarChange(position, value, this, incidents);
            }
        });

        convertView.setOnClickListener(v -> clickableIncident.onClickItem(incidents, position));

        return convertView;
    }

    private int getVehicleIconResource(Incident incident) {
        if (incident.getVehiculeType() == null) {
            return 0;
        }

        return getContext().getResources().getIdentifier(
                incident.getVehiculeType().getImage(),
                "drawable",
                getContext().getPackageName()
        );
    }

    private String getIncidentTitle(Incident incident) {
        String vehicleName = "Incident";

        if (incident.getVehiculeType() != null) {
            vehicleName = incident.getVehiculeType().getVehiculeName();
        }

        return vehicleName + " - " + formatDistance(incident.getDistanceKm()) + " km";
    }

    private String formatDistance(double distanceKm) {
        if (distanceKm == Math.rint(distanceKm)) {
            return String.format(Locale.US, "%.0f", distanceKm);
        }

        return String.format(Locale.US, "%.1f", distanceKm);
    }

    private static class ViewHolder {
        private final ImageView icon;
        private final TextView title;
        private final TextView address;
        private final RatingBar status;

        private ViewHolder(View view) {
            icon = view.findViewById(R.id.incidentIcon);
            title = view.findViewById(R.id.incidentTitle);
            address = view.findViewById(R.id.incidentAddress);
            status = view.findViewById(R.id.incidentStatus);
        }
    }
}
