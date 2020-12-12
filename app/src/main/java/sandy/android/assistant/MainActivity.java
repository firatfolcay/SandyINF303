package sandy.android.assistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    DatabaseManagement db;
    LinearLayoutManager linearLayoutManager;
    LinearLayoutManager notebooksLinearLayoutManager;
    ConstraintLayout mainActivityConstraintLayout;
    RecyclerView listOfNotes;
    RecyclerView listOfNotebooks;
    NavigationView notebookNavigationView;
    ImageView mainActivityNavigationViewImageView;
    FloatingActionButton fab_create_new_note;
    ImageView buttonShowNotification;
    ArrayList<Note> notes;
    ArrayList<Notebook> notebooks;
    NoteAdapter noteAdapter;
    MainActivityNavigationDrawerAdapter mainActivityNavigationDrawerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseManagement(this);

        fab_create_new_note = (FloatingActionButton) findViewById(R.id.fab_create_new_note);

        mainActivityConstraintLayout = (ConstraintLayout) findViewById(R.id.mainActivityConstraintLayout);
        mainActivityNavigationViewImageView = (ImageView) findViewById(R.id.mainActivityNavigationViewImageView);
        notebookNavigationView = (NavigationView) findViewById(R.id.mainActivityNavigationView);

        buttonShowNotification = findViewById(R.id.showNotification);

        listOfNotes = findViewById(R.id.listOfNotes);
        listOfNotebooks = findViewById(R.id.mainActivityListOfNotebooks);


        notes = db.getAllNotes();
        noteAdapter = new NoteAdapter(this, notes, db, this);       //create new Adapter to fetch Notes from DB and to show them in Cardview inside Recycleview
        listOfNotes.setAdapter(noteAdapter);

        notebooks = db.getAllNotebooks();
        mainActivityNavigationDrawerAdapter = new MainActivityNavigationDrawerAdapter(this, notebooks, db);
        listOfNotebooks.setAdapter(mainActivityNavigationDrawerAdapter);

        linearLayoutManager = new LinearLayoutManager(this);        //defines LinearLayoutManager for vertical Notes Recycleview orientation
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listOfNotes.setLayoutManager(linearLayoutManager);

        notebooksLinearLayoutManager = new LinearLayoutManager(this);
        notebooksLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listOfNotebooks.setLayoutManager(notebooksLinearLayoutManager);

        // Unused variables ??
        ConstraintLayout mainActivityConstraintLayout = findViewById(R.id.mainActivityConstraintLayout);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        buttonShowNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NotificationViewActivity.class);
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

        mainActivityConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notebookNavigationView.getVisibility() == View.VISIBLE) {
                    notebookNavigationView.setVisibility(View.INVISIBLE);
                }
            }
        });

        listOfNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notebookNavigationView.getVisibility() == View.VISIBLE) {
                    notebookNavigationView.setVisibility(View.INVISIBLE);
                }
            }
        });


        mainActivityNavigationViewImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notebookNavigationView.setVisibility(View.VISIBLE);
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

    @Override
    public void onBackPressed() {       //overrides back button behavior
        if (notebookNavigationView.getVisibility() == View.VISIBLE) {       //if navigation drawer is opened
            notebookNavigationView.setVisibility(View.INVISIBLE);           //simply close it
        } else {
            super.onBackPressed();      //if navigation drawer is already closed, apply primary activity behavior
        }
    }

    public void startNoteEditorActivity () {
        Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);        //creates new intent that opens up note_editor.xml screen and runs NoteEditorActivity.java
        startActivity(intent);
    }
}
