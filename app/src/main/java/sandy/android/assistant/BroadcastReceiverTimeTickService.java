package sandy.android.assistant;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class BroadcastReceiverTimeTickService extends Service {
    private static BroadcastReceiver m_TimeTickReceiver;

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        registerTimeTickReceiver();
    }

    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        unregisterReceiver(m_TimeTickReceiver);
        m_TimeTickReceiver = null;
    }

    private void registerTimeTickReceiver()
    {
        m_TimeTickReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                Log.d(TAG, "ACTION_TIME_TICK");
                System.out.println("time tick");
                // do something, e.g. send Intent to main app

            }
        };
        IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
        registerReceiver(m_TimeTickReceiver, filter);
    }
}
