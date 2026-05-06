package edu.iut.filrouge;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class MenuFragment extends Fragment {

    private static final String ARG_MENU = "menuActif";

    private Menuable menuable;
    private int currentActivatedIndex = 0;

    private ImageButton[] buttons;

    public MenuFragment() {
    }

    public static MenuFragment newInstance(int menuActif) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MENU, menuActif);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (requireActivity() instanceof Menuable) {
            menuable = (Menuable) requireActivity();
        } else {
            throw new AssertionError("Classe " + requireActivity().getClass().getName() + " ne met pas en œuvre Menuable.");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            currentActivatedIndex = getArguments().getInt(ARG_MENU, 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        buttons = new ImageButton[]{
                view.findViewById(R.id.buttonMenu0),
                view.findViewById(R.id.buttonMenu1),
                view.findViewById(R.id.buttonMenu2),
                view.findViewById(R.id.buttonMenu3),
                view.findViewById(R.id.buttonMenu4),
                view.findViewById(R.id.buttonMenu5),
                view.findViewById(R.id.buttonMenu6)
        };

        updateMenuImages();

        for (int i = 0; i < buttons.length; i++) {
            int index = i;
            buttons[i].setOnClickListener(v -> {
                currentActivatedIndex = index;
                updateMenuImages();
                menuable.onMenuChange(index);
            });
        }

        return view;
    }

    private void updateMenuImages() {
        for (int i = 0; i < buttons.length; i++) {
            int resId;
            if (i == currentActivatedIndex) {
                resId = getResources().getIdentifier("menu" + i + "_s", "drawable", requireContext().getPackageName());
            } else {
                resId = getResources().getIdentifier("menu" + i, "drawable", requireContext().getPackageName());
            }
            buttons[i].setImageResource(resId);
        }
    }
}