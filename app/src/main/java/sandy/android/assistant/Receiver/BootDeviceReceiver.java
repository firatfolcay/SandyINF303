package sandy.android.assistant.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.service.notification.NotificationListenerService;

import sandy.android.assistant.Controller.MainActivity;

public class BootDeviceReceiver extends BroadcastReceiver {

    public static boolean BOOT_HAPPENED = false;

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if(Intent.ACTION_BOOT_COMPLETED.equals(action))
        {
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
