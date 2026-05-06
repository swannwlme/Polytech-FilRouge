package edu.iut.filrouge;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class Screen1Fragment extends Fragment {

    private Notifiable notifiable;

    public Screen1Fragment() {
    }

    public static Screen1Fragment newInstance(String param1, String param2) {
        Screen1Fragment fragment = new Screen1Fragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Notifiable) {
            notifiable = (Notifiable) context;
        } else {
            throw new RuntimeException(context.toString() + " doit implémenter Notifiable");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_screen1, container, false);

        TextView title = view.findViewById(R.id.title);
        title.setText("Main Fragment");

        Button buttonGo = view.findViewById(R.id.buttonGo);
        buttonGo.setOnClickListener(v -> {
            if (notifiable != null) {
                notifiable.onClick(1);
            }
        });

        return view;
    }
}