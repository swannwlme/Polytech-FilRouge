package edu.iut.filrouge.controller;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import edu.iut.filrouge.R;
import edu.iut.filrouge.view.MainActivity;

public class FilRougeFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FilRougeFCM";
    private static final AtomicInteger NEXT_NOTIFICATION_ID =
            new AtomicInteger((int) (System.currentTimeMillis() & 0x0FFFFFFF));

    @Override
    public void onRegistered(@NonNull String installationId) {
        super.onRegistered(installationId);
        Log.d(TAG, "Instance enregistrée auprès de FCM : " + installationId);
        // En production, cet identifiant serait transmis au backend de l'application.
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String, String> data = remoteMessage.getData();
        RemoteMessage.Notification notificationPayload = remoteMessage.getNotification();

        String title = firstNonBlank(
                data.get("titre"),
                data.get("title"),
                data.get("notification_title"),
                notificationPayload != null ? notificationPayload.getTitle() : null,
                getString(R.string.notification_default_title)
        );
        String body = firstNonBlank(
                data.get("corps"),
                data.get("body"),
                data.get("message"),
                data.get("content"),
                data.get("contenu"),
                notificationPayload != null ? notificationPayload.getBody() : null,
                getString(R.string.notification_default_body)
        );

        showNotification(title, body);
    }

    private void showNotification(String title, String body) {
        NotificationChannelHelper.createIncidentChannel(this);

        if (!canPostNotifications()) {
            Log.w(TAG, "Notification reçue, mais l'autorisation POST_NOTIFICATIONS n'est pas accordée.");
            return;
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                this,
                NotificationChannelHelper.getIncidentChannelId(this)
        )
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat.from(this).notify(
                NEXT_NOTIFICATION_ID.incrementAndGet(),
                builder.build()
        );
    }

    private boolean canPostNotifications() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return true;
        }
        return checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED;
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (!TextUtils.isEmpty(value) && !TextUtils.isEmpty(value.trim())) {
                return value.trim();
            }
        }
        return "";
    }
}
