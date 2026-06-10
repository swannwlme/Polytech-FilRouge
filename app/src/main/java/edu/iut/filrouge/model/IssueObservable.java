package edu.iut.filrouge.model;

public interface IssueObservable {
    void addObserver(IssueObserver observer);
    void removeObserver(IssueObserver observer);
    void notifyObservers();
}
