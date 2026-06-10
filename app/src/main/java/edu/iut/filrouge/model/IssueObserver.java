package edu.iut.filrouge.model;

public interface IssueObserver {
    void onStatusChanged(Incident incident);
    void onPriorityChanged(Incident incident);
}
