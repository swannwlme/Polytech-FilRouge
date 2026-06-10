package edu.iut.filrouge.controller;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

import java.util.List;

import edu.iut.filrouge.model.Incident;
import edu.iut.filrouge.model.IssueManager;
import edu.iut.filrouge.view.Screen4Fragment;

public class IssueController {

    private final Screen4Fragment view;
    private final IssueManager model;

    public IssueController(Screen4Fragment view, IssueManager model) {
        this.view = view;
        this.model = model;
    }

    public List<Incident> getIncidents() {
        return model.getIncidents();
    }

    public void configureMarker(Marker marker, Incident incident) {
        marker.setRelatedObject(incident);
        marker.setDraggable(true);
        marker.setOnMarkerClickListener((clickedMarker, clickedMapView) -> {
            view.toggleMarker(clickedMarker);
            clickedMapView.getController().animateTo(clickedMarker.getPosition());
            return true;
        });
        marker.setOnMarkerDragListener(new Marker.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(Marker draggedMarker) {
            }

            @Override
            public void onMarkerDragEnd(Marker draggedMarker) {
                updateIncidentLocation(draggedMarker);
                view.showMarkerInfo(draggedMarker);
            }

            @Override
            public void onMarkerDragStart(Marker draggedMarker) {
                draggedMarker.closeInfoWindow();
            }
        });
    }

    public void onIncidentSelected(Incident incident) {
        view.toggleIncidentMarker(incident);
    }

    private void updateIncidentLocation(Marker marker) {
        Object relatedObject = marker.getRelatedObject();

        if (!(relatedObject instanceof Incident)) {
            return;
        }

        GeoPoint position = marker.getPosition();
        model.setLocation(
                (Incident) relatedObject,
                position.getLatitude(),
                position.getLongitude()
        );
    }
}
