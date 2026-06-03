package edu.iut.filrouge;

public interface IssueObserver {
    void onStatusChanged(Incident incident);
    void onPriorityChanged(Incident incident);
}
