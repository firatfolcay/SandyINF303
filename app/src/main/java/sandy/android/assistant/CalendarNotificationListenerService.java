package sandy.android.assistant;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;

public class CalendarNotificationListenerService extends NotificationListenerService {

    @Override
    public void onCreate() {
        System.out.println("notificationlistener service created.");
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        final String packageName = sbn.getPackageName();
        if (!TextUtils.isEmpty(packageName) && packageName.equals("com.android.calendar")) {
            // Do something
            System.out.println("calendar event.");
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        // Nothing to do
    }

}
