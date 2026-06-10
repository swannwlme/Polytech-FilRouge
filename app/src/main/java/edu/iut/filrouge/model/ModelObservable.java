package edu.iut.filrouge.model;

public interface ModelObservable {
    void addObserver(ViewObserver observer);

    void removeObserver(ViewObserver observer);

    void notifyObservers();
}
