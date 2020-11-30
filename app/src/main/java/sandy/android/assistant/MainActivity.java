package sandy.android.assistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.solver.state.State;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
    LinearLayoutManager linearLayoutManager;
    RecyclerView listOfNotes;
    FloatingActionButton fab_create_new_note;
    ImageView buttonShowNotification;
    ArrayList<Note> notes;
    NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseManagement(this);

        fab_create_new_note = (FloatingActionButton) findViewById(R.id.fab_create_new_note);

        buttonShowNotification = findViewById(R.id.showNotification);

        listOfNotes = findViewById(R.id.listOfNotes);

        notes = db.getAllNotes();
        noteAdapter = new NoteAdapter(this, notes, db, this);       //create new Adapter to fetch Notes from DB and to show them in Cardview inside Recycleview
        listOfNotes.setAdapter(noteAdapter);

        linearLayoutManager = new LinearLayoutManager(this);        //defines LinearLayoutManager for vertical Recycleview orientation
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listOfNotes.setLayoutManager(linearLayoutManager);

        // Unused variables ??
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

    @Override
    protected void onResume() {
        super.onResume();
        notes = db.getAllNotes();
        noteAdapter = new NoteAdapter(this, notes, db, this);
        listOfNotes.setAdapter(noteAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_layout_options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case R.id.linearViewVertical:
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                listOfNotes.setLayoutManager(linearLayoutManager);
                break;
            case R.id.gridView:
                GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
                listOfNotes.setLayoutManager(gridLayoutManager);
                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
                listOfNotes.setLayoutManager(staggeredGridLayoutManager);
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    public void startNoteEditorActivity () {
        Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);        //creates new intent that opens up note_editor.xml screen and runs NoteEditorActivity.java
        startActivity(intent);
    }
}
