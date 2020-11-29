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
import android.widget.ImageView;

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


        FloatingActionButton fab_create_new_note = (FloatingActionButton) findViewById(R.id.fab_create_new_note);

        ImageView buttonShowNotification = findViewById(R.id.showNotification);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        buttonShowNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getApplicationContext(),NotificationController.class);
                startActivity(intent);
            }
        });

        fab_create_new_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //setContentView(R.layout.note_editor);
                Intent intent = new Intent(getApplicationContext(), NoteEditorController.class);        //creates new intent that opens up note_editor.xml screen and runs NoteEditorController.java
                startActivity(intent);
                //nec.onCreate(savedInstanceState);

            }


        });




    }










    }
