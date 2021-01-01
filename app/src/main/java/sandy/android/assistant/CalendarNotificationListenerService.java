package sandy.android.assistant;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.Html;
import android.text.TextUtils;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;

public class CalendarNotificationListenerService extends NotificationListenerService {
    NotificationChannel channel;
    NotificationManager notificationManager;

    @Override
    public void onCreate() {
        System.out.println("notificationlistener service created.");
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        final String packageName = sbn.getPackageName();
        if (!TextUtils.isEmpty(packageName) && packageName.equals("com.android.calendar")) {
            // Do something
            createNotificationChannel();

            Bundle extras = sbn.getNotification().extras;
            String noteTitle = extras.get(Notification.EXTRA_TITLE).toString();
            System.out.println("note title:" + noteTitle);
            //String noteContent = extras.get(Notification.EXTRA_BIG_TEXT).toString();
            DatabaseManagement db;
            db = new DatabaseManagement(this);
            ArrayList<Note> dbNotes = db.getAllNotes();
            if (dbNotes.size() > 0) {
                for (int i = 0; i < dbNotes.size(); i++) {
                    if (dbNotes.get(i).getTitle().equals(noteTitle)) {
                        Intent intent = new Intent(getApplicationContext(), NotePreviewActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("calendar_notification_note_title", dbNotes.get(i).getTitle());
                        intent.putExtra("calendar_notification_note_content", dbNotes.get(i).getContent());
                        intent.setData((Uri.parse("custom://"+System.currentTimeMillis())));
                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), dbNotes.get(i).getNotification().getId(), intent, 0);

                        NotificationCompat.BigTextStyle nc;
                        android.app.Notification.Builder builder = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            builder = new android.app.Notification.Builder(this, "notification_channel_" + Integer.toString(dbNotes.get(i).getId()));
                        }
                        else {
                            builder = new android.app.Notification.Builder(this);
                        }
                        builder.setContentTitle(dbNotes.get(i).getTitle());
                        builder.setContentText(dbNotes.get(i).getTitle());
                        builder.setSmallIcon(R.drawable.circle_clock);
                        builder.setStyle(new android.app.Notification.BigTextStyle().bigText(Html.fromHtml(dbNotes.get(i).getContent())));
                        builder.setContentIntent(pendingIntent);
                        builder.setAutoCancel(true);

                        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(dbNotes.get(i).getNotification().getId(), builder.build());
                    }
                }
            }
            System.out.println("calendar event.");
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        // Nothing to do
    }

    public void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            channel = new NotificationChannel(getString(R.string.channel_name), name, importance);
            channel.setDescription(description);
            channel.setLightColor(Color.BLUE);
            //channel.setLockscreenVisibility(android.app.Notification.VISIBILITY_PUBLIC);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            System.out.println("notification channel name: " + notificationManager.getNotificationChannel(channel.getId()).getId());
        }
    }

}
