package edu.iut.filrouge;

public interface Notifiable {
    void onClick(int numFragment);

    void onDataChange(int numFragment, Object object, int actionCode, Object argsAction);

    void onFragmentDisplayed(int fragmentId);
}
