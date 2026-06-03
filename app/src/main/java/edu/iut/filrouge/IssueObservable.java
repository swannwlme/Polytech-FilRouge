package edu.iut.filrouge;

public interface IssueObservable {
    void addObserver(IssueObserver observer);
    void removeObserver(IssueObserver observer);
    void notifyObservers();
}
