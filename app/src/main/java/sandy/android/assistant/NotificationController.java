package sandy.android.assistant;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationController extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
        createNotificationChannel();

        ImageView buttonMainActivity = findViewById(R.id.mainActivity);
        Button denemeNotification = findViewById(R.id.alarmbutton);
        Button buttonNotificationEditor = (Button) findViewById(R.id.notificationEditorButton);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"channel1")
                .setContentTitle("Alarm Notification")
                .setContentText("Aga kalk düğüne geç kaldın")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        buttonMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        buttonNotificationEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),NotificationEditorActivity.class);
                startActivity(intent);
            }
        });

        denemeNotification.setOnClickListener(new View.OnClickListener() { //notification test
            @Override
            public void onClick(View v) {
                notificationManager.notify(100,builder.build());

            }
        });




    }


    private void createNotificationChannel(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "denemeChannel";
            String description="denemek icin kanal";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("ch1",name,importance);
            channel.setDescription(description);
        }
    }
}