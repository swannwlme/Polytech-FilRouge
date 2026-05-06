package edu.iut.filrouge;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class ControlActivity extends AppCompatActivity implements Notifiable, Menuable {

    private static final int FRAGMENT_COUNT = 7;

    private int menuActif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        menuActif = sanitizeFragmentIndex(getIntent().getIntExtra("menu", 0));

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.menuFragmentContainer, MenuFragment.newInstance(menuActif));
            transaction.replace(R.id.screenFragmentContainer, createFragment(menuActif));
            transaction.commit();
        }

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.screenFragmentContainer);

            if (fragment instanceof Screen1Fragment) {
                menuActif = 0;
            } else if (fragment instanceof Screen2Fragment) {
                menuActif = 1;
            } else if (fragment instanceof Screen3Fragment) {
                menuActif = 2;
            } else if (fragment instanceof Screen4Fragment) {
                menuActif = 3;
            } else if (fragment instanceof Screen5Fragment) {
                menuActif = 4;
            } else if (fragment instanceof Screen6Fragment) {
                menuActif = 5;
            } else if (fragment instanceof Screen7Fragment) {
                menuActif = 6;
            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.menuFragmentContainer, MenuFragment.newInstance(menuActif));
            transaction.commit();
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    finish();
                }
            }
        });
    }

    @Override
    public void onClick(int numFragment) {
        Log.d("ControlActivity", "Le bouton GO du Screen" + numFragment + "Fragment a été cliqué");
    }

    @Override
    public void onDataChange(int numFragment, Object object, int actionCode, Object argsAction) {
        if (numFragment == Screen2Fragment.FRAGMENT_ID
                && actionCode == Screen2Fragment.ACTION_OPEN_DETAILS
                && object instanceof Incident) {
            Fragment fragment = Screen1Fragment.newInstance((Incident) object);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.screenFragmentContainer, fragment);
            transaction.addToBackStack(null);
            transaction.commit();

            menuActif = Screen1Fragment.FRAGMENT_ID;

            FragmentTransaction transactionMenu = getSupportFragmentManager().beginTransaction();
            transactionMenu.replace(R.id.menuFragmentContainer, MenuFragment.newInstance(menuActif));
            transactionMenu.commit();
            return;
        }

        if (numFragment == Screen2Fragment.FRAGMENT_ID
                && actionCode == Screen2Fragment.ACTION_STATUS_CHANGED
                && object instanceof Incident) {
            Incident incident = (Incident) object;
            Log.d("ControlActivity", "Statut modifié : "
                    + incident.getVehiculeType().getVehiculeName()
                    + " = " + incident.getStatus());
            return;
        }

        if (numFragment >= 0 && numFragment < FRAGMENT_COUNT) {
            Fragment fragment = createFragment(numFragment);

            if (object instanceof Bundle) {
                fragment.setArguments((Bundle) object);
            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.screenFragmentContainer, fragment);
            transaction.addToBackStack(null);
            transaction.commit();

            menuActif = numFragment;

            FragmentTransaction transactionMenu = getSupportFragmentManager().beginTransaction();
            transactionMenu.replace(R.id.menuFragmentContainer, MenuFragment.newInstance(menuActif));
            transactionMenu.commit();
        }
    }

    @Override
    public void onFragmentDisplayed(int fragmentId) {
        Log.d("ControlActivity", "Fragment affiché : " + fragmentId);
    }

    @Override
    public void onMenuChange(int index) {
        menuActif = sanitizeFragmentIndex(index);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.screenFragmentContainer, createFragment(menuActif));
        transaction.replace(R.id.menuFragmentContainer, MenuFragment.newInstance(menuActif));
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private Fragment createFragment(int index) {
        switch (index) {
            case 0:
                return new Screen1Fragment();
            case 1:
                return new Screen2Fragment();
            case 2:
                return new Screen3Fragment();
            case 3:
                return new Screen4Fragment();
            case 4:
                return new Screen5Fragment();
            case 5:
                return new Screen6Fragment();
            case 6:
                return new Screen7Fragment();
            default:
                return new Screen1Fragment();
        }
    }

    private int sanitizeFragmentIndex(int index) {
        if (index < 0 || index >= FRAGMENT_COUNT) {
            return 0;
        }

        return index;
    }
}
