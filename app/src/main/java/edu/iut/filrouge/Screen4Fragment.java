package edu.iut.filrouge;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Screen4Fragment extends Fragment {

    public static final int FRAGMENT_ID = 3;

    private static final double DEFAULT_ZOOM = 15.5;

    private Notifiable notifiable;
    private MapView mapView;
    private List<Incident> incidents;
    private MapIncidentAdapter mapIncidentAdapter;
    private final List<Marker> markers = new ArrayList<>();

    public Screen4Fragment() {
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
        Context appContext = requireContext().getApplicationContext();
        org.osmdroid.config.Configuration.getInstance().setUserAgentValue(appContext.getPackageName());

        View view = inflater.inflate(R.layout.fragment_screen4, container, false);

        incidents = IncidentRepository.getIncidents();
        mapView = view.findViewById(R.id.incidentsMap);
        ListView incidentList = view.findViewById(R.id.mapIncidentList);

        configureMap();
        addIncidentMarkers();
        mapIncidentAdapter = new MapIncidentAdapter(requireContext(), incidents);
        incidentList.setAdapter(mapIncidentAdapter);
        incidentList.setOnItemClickListener((parent, itemView, position, id) ->
                toggleIncidentMarker(incidents.get(position)));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (notifiable != null) {
            notifiable.onFragmentDisplayed(FRAGMENT_ID);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        if (mapView != null) {
            mapView.onPause();
        }

        super.onPause();
    }

    @Override
    public void onDestroyView() {
        markers.clear();

        if (mapView != null) {
            mapView.onDetach();
            mapView = null;
        }

        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        notifiable = null;
    }

    private void configureMap() {
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        mapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT);
        mapView.getController().setZoom(DEFAULT_ZOOM);
        mapView.getController().setCenter(getMapCenter());
    }

    private void addIncidentMarkers() {
        for (Incident incident : incidents) {
            Drawable markerIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_location_pin);
            Marker marker = new Marker(mapView);
            marker.setPosition(new GeoPoint(incident.getLatitude(), incident.getLongitude()));
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setIcon(markerIcon);
            marker.setTitle(getIncidentLabel(incident));
            marker.setSnippet(incident.getAdresse() + "\n" + formatCoordinates(incident));
            marker.setRelatedObject(incident);
            marker.setDraggable(true);
            marker.setOnMarkerClickListener((clickedMarker, clickedMapView) -> {
                toggleMarker(clickedMarker);
                clickedMapView.getController().animateTo(clickedMarker.getPosition());
                return true;
            });
            marker.setOnMarkerDragListener(new Marker.OnMarkerDragListener() {
                @Override
                public void onMarkerDrag(Marker draggedMarker) {
                    updateIncidentPosition(draggedMarker, false);
                }

                @Override
                public void onMarkerDragEnd(Marker draggedMarker) {
                    updateIncidentPosition(draggedMarker, true);
                    draggedMarker.showInfoWindow();
                    mapView.invalidate();
                }

                @Override
                public void onMarkerDragStart(Marker draggedMarker) {
                    draggedMarker.closeInfoWindow();
                }
            });

            markers.add(marker);
            mapView.getOverlays().add(marker);
        }

        mapView.invalidate();
    }

    private GeoPoint getMapCenter() {
        if (incidents.isEmpty()) {
            return new GeoPoint(IncidentFactory.DEFAULT_LATITUDE, IncidentFactory.DEFAULT_LONGITUDE);
        }

        double latitude = 0.0;
        double longitude = 0.0;

        for (Incident incident : incidents) {
            latitude += incident.getLatitude();
            longitude += incident.getLongitude();
        }

        return new GeoPoint(latitude / incidents.size(), longitude / incidents.size());
    }

    private void toggleIncidentMarker(Incident incident) {
        for (Marker marker : markers) {
            if (marker.getRelatedObject() == incident) {
                toggleMarker(marker);
                mapView.getController().animateTo(marker.getPosition());
                return;
            }
        }
    }

    private void toggleMarker(Marker marker) {
        boolean wasShown = marker.isInfoWindowShown();
        closeInfoWindows();

        if (!wasShown) {
            marker.showInfoWindow();
        }

        mapView.invalidate();
    }

    private void updateIncidentPosition(Marker marker, boolean refreshList) {
        Object relatedObject = marker.getRelatedObject();

        if (!(relatedObject instanceof Incident)) {
            return;
        }

        Incident incident = (Incident) relatedObject;
        GeoPoint position = marker.getPosition();
        incident.setLatitude(position.getLatitude());
        incident.setLongitude(position.getLongitude());
        marker.setSnippet(incident.getAdresse() + "\n" + formatCoordinates(incident));

        if (refreshList && mapIncidentAdapter != null) {
            mapIncidentAdapter.notifyDataSetChanged();
        }
    }

    private void closeInfoWindows() {
        for (Marker marker : markers) {
            marker.closeInfoWindow();
        }
    }

    private static String getIncidentLabel(Incident incident) {
        String description = incident.getDescription();

        if (description != null && !description.trim().isEmpty()) {
            return description;
        }

        if (incident.getVehiculeType() != null) {
            return incident.getVehiculeType().getVehiculeName();
        }

        return "Incident";
    }

    private static String formatCoordinates(Incident incident) {
        return String.format(Locale.US, "%.4f, %.4f", incident.getLatitude(), incident.getLongitude());
    }

    private static int getVehicleIconResource(Context context, Incident incident) {
        if (incident.getVehiculeType() == null) {
            return 0;
        }

        return context.getResources().getIdentifier(
                incident.getVehiculeType().getImage(),
                "drawable",
                context.getPackageName()
        );
    }

    private static class MapIncidentAdapter extends ArrayAdapter<Incident> {

        private final LayoutInflater inflater;

        private MapIncidentAdapter(@NonNull Context context, @NonNull List<Incident> incidents) {
            super(context, 0, incidents);
            inflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_map_incident, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Incident incident = getItem(position);

            if (incident != null) {
                holder.icon.setImageResource(getVehicleIconResource(getContext(), incident));
                holder.title.setText(getIncidentLabel(incident) + " (" + formatCoordinates(incident) + ")");
                holder.address.setText(incident.getAdresse());
            }

            return convertView;
        }
    }

    private static class ViewHolder {
        private final ImageView icon;
        private final TextView title;
        private final TextView address;

        private ViewHolder(View view) {
            icon = view.findViewById(R.id.mapIncidentIcon);
            title = view.findViewById(R.id.mapIncidentTitle);
            address = view.findViewById(R.id.mapIncidentAddress);
        }
    }
}
