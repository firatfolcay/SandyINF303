package sandy.android.assistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    View view;
    NoteEditorController nec = new NoteEditorController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();

        FloatingActionButton fab_create_new_note = (FloatingActionButton) findViewById(R.id.fab_create_new_note);
        Button buttonShowNotification = findViewById(R.id.showbutton);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"channel1")
                .setSmallIcon(R.drawable.ic_baseline_access_alarm_24)
                .setContentTitle("Alarm Notification")
                .setContentText("Aga kalk düğüne geç kaldın")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        fab_create_new_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationManager.notify(100,builder.build());
                //setContentView(R.layout.note_editor);
                Intent intent = new Intent(getApplicationContext(), NoteEditorController.class);        //creates new intent that opens up note_editor.xml screen and runs NoteEditorController.java
                startActivity(intent);
                //nec.onCreate(savedInstanceState);

            }

        });




    }
    private void createNotificationChannel(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "denemeChannel";
            String description="denemek icin kanal";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("ch1",name,importance);
            channel.setDescription(description);
        }
    }









    }
