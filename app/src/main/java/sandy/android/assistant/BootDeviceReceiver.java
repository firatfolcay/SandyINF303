package sandy.android.assistant;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.service.notification.NotificationListenerService;
import android.util.Log;
import android.widget.Toast;

public class BootDeviceReceiver extends BroadcastReceiver {

    private static final String TAG_BOOT_BROADCAST_RECEIVER = "BOOT_BROADCAST_RECEIVER";
    public static boolean BOOT_HAPPENED = false;

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if(Intent.ACTION_BOOT_COMPLETED.equals(action))
        {
            //startServiceDirectly(context);

            //startServiceByAlarm(context);
            //BOOT_HAPPENED = false;
            System.out.println("boot happened: " + BOOT_HAPPENED);
            startServices(context);
            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }

    public void startServices(Context context) {

        Intent startServiceIntent4 = new Intent(context, NotificationListenerService.class);
        context.startService(startServiceIntent4);

    }
}
