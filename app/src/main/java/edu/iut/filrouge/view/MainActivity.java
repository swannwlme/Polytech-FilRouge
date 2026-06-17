package edu.iut.filrouge.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.messaging.FirebaseMessaging;

import edu.iut.filrouge.R;
import edu.iut.filrouge.controller.ControlActivity;
import edu.iut.filrouge.controller.NotificationChannelHelper;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "FilRougeFCM";
    private static final int REQUEST_POST_NOTIFICATIONS = 1001;

    private ImageView imageViewAnimation;
    private Button buttonOption1;
    private Button defaultButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        applySafeAreaInsets();
        NotificationChannelHelper.createIncidentChannel(this);
        requestPostNotificationsPermission();
        registerToFirebaseMessaging();

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

    private void applySafeAreaInsets() {
        View root = findViewById(R.id.main);
        int initialPaddingLeft = root.getPaddingLeft();
        int initialPaddingTop = root.getPaddingTop();
        int initialPaddingRight = root.getPaddingRight();
        int initialPaddingBottom = root.getPaddingBottom();

        ViewCompat.setOnApplyWindowInsetsListener(root, (view, windowInsets) -> {
            Insets systemBars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(
                    initialPaddingLeft + systemBars.left,
                    initialPaddingTop + systemBars.top,
                    initialPaddingRight + systemBars.right,
                    initialPaddingBottom + systemBars.bottom
            );
            return windowInsets;
        });
        ViewCompat.requestApplyInsets(root);
    }

    private void requestPostNotificationsPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED) {
            return;
        }

        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.POST_NOTIFICATIONS},
                REQUEST_POST_NOTIFICATIONS
        );
    }

    private void registerToFirebaseMessaging() {
        FirebaseMessaging.getInstance().register()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Impossible d'enregistrer l'instance auprès de FCM", task.getException());
                        return;
                    }

                    Log.d(TAG, "Demande d'enregistrement FCM terminée");
                });
    }
}
