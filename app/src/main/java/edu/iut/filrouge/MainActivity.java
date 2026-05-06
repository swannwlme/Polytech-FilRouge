package edu.iut.filrouge;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private ImageView imageViewAnimation;
    private Button buttonOption1;
    private Button defaultButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageViewAnimation = findViewById(R.id.imageViewAnimation);

        AnimationDrawable animation = (AnimationDrawable) imageViewAnimation.getBackground();
        animation.start();

        buttonOption1 = findViewById(R.id.buttonOption1);
        defaultButton = findViewById(R.id.buttonDefault);

        defaultButton.setOnClickListener(v -> {
            animation.stop();
            Intent intent = new Intent(MainActivity.this, ControlActivity.class);
            startActivity(intent);
        });

        buttonOption1.setOnClickListener(v -> {
            animation.stop();
            Intent intent = new Intent(MainActivity.this, ControlActivity.class);
            intent.putExtra("menu", 3);
            startActivity(intent);
        });
    }
}