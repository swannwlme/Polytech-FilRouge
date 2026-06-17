package edu.iut.filrouge.controller;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import edu.iut.filrouge.R;

public final class NotificationChannelHelper {

    private NotificationChannelHelper() {
    }

    public static String getIncidentChannelId(Context context) {
        return context.getString(R.string.notification_channel_id);
    }

    public static void createIncidentChannel(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }

        NotificationChannel channel = new NotificationChannel(
                getIncidentChannelId(context),
                context.getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
        );
        channel.setDescription(context.getString(R.string.notification_channel_description));

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        }
    }
}
