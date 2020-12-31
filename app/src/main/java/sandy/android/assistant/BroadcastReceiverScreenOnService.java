package sandy.android.assistant;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class BroadcastReceiverScreenOnService extends Service {
    private static BroadcastReceiver m_ScreenOnReceiver;

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        registerScreenOnReceiver();
    }

    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        unregisterReceiver(m_ScreenOnReceiver);
        m_ScreenOnReceiver = null;
    }

    private void registerScreenOnReceiver()
    {
        m_ScreenOnReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                Log.d(TAG, "ACTION_SCREEN_ON");
                System.out.println("screen on");
                // do something, e.g. send Intent to main app

            }
        };
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        registerReceiver(m_ScreenOnReceiver, filter);
    }
}
