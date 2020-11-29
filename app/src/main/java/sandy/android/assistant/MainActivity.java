package sandy.android.assistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.solver.state.State;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    DatabaseManagement db;
    Toolbar toolbar;
    RecyclerView recyclerView;
    View view;
    NoteEditorActivity nec = new NoteEditorActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseManagement(this);


        FloatingActionButton fab_create_new_note = (FloatingActionButton) findViewById(R.id.fab_create_new_note);

        ImageView buttonShowNotification = findViewById(R.id.showNotification);
        RecyclerView listOfNotes = findViewById(R.id.listOfNotes);
        ArrayList<Note> notesFromDB = new ArrayList<Note>();
        notesFromDB = db.getAllNotes();
        NoteAdapter noteAdapter = new NoteAdapter(this, notesFromDB);       //create new Adapter to fetch Notes from DB and to show them in Cardview inside Recycleview
        listOfNotes.setAdapter(noteAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);        //defines LinearLayoutManager for vertical Recycleview orientation
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listOfNotes.setLayoutManager(linearLayoutManager);
        ConstraintLayout mainActivityConstraintLayout = findViewById(R.id.mainActivityConstraintLayout);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        buttonShowNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setContentView(R.layout.notification);
                Intent intent = new Intent(getApplicationContext(),Notification.class);
                startActivity(intent);
            }
        });

        fab_create_new_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);        //creates new intent that opens up note_editor.xml screen and runs NoteEditorActivity.java
                startActivity(intent);

            }


        });




    }










    }
