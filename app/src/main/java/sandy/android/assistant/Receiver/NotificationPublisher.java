package sandy.android.assistant.Receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import sandy.android.assistant.Model.Notification;

public class NotificationPublisher extends BroadcastReceiver {
    public static String NOTIFICATION_ID = "notification_id";
    public static String NOTIFICATION = "notification";

    public void onReceive(Context context, Intent intent) {

        /*NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        android.app.Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        /*notification.flags += android.app.Notification.FLAG_ONGOING_EVENT;
        notification.flags += android.app.Notification.FLAG_NO_CLEAR;
        notificationManager.notify(id, notification);*/

    }

    public void deleteNotification(Context context, Notification n) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(n.getId());
    }
}
