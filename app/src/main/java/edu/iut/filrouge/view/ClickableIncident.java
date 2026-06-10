package edu.iut.filrouge.view;

import android.content.Context;

import java.util.List;

public interface ClickableIncident<T> {
    void onRatingBarChange(int itemIndex, float value, IncidentAdapter adapter, List<T> items);

    void onClickItem(List<T> items, int itemIndex);

    Context getContext();
}
