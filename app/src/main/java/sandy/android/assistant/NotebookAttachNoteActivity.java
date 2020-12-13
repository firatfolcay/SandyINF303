package sandy.android.assistant;

import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.irshulx.Editor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class NotebookAttachNoteActivity extends AppCompatActivity {

    DatabaseManagement db;
    LinearLayoutManager linearLayoutManager;
    RecyclerView listOfNotesToAttach;
    ArrayList<Note> notesWithNoNotebookAttached;
    NotebookAttachNoteAdapter notebookAttachNoteAdapter;

    ConstraintLayout notebookEditorViewConstraintLayout;
    Editor notebookEditorView;
    Button attachSelectedNotesButton;
    Button cancelAttachButton;
    CardView itemNotebookAttachNoteCardview;

    View notesToAttachView;

    LayoutInflater layoutInflater;

    String selectedNotebookIdString;
    Notebook selectedNotebook;

    String notebookEditorViewHtmlString;
    TextView notebookTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notebook_attach_note_view);

        listOfNotesToAttach = findViewById(R.id.listOfNotesToAttach);
        attachSelectedNotesButton = findViewById(R.id.buttonAttachSelectedNotes);
        cancelAttachButton = findViewById(R.id.buttonCancelAttach);

        db = new DatabaseManagement(this);

        notesWithNoNotebookAttached = db.getAllNotesWithNoNotebooksAttached();
        notebookAttachNoteAdapter = new NotebookAttachNoteAdapter(this, notesWithNoNotebookAttached, db);
        listOfNotesToAttach.setAdapter(notebookAttachNoteAdapter);

        linearLayoutManager = new LinearLayoutManager(this);        //defines LinearLayoutManager for vertical Notes Recycleview orientation
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listOfNotesToAttach.setLayoutManager(linearLayoutManager);

        attachSelectedNotesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int index = 0; index < notebookAttachNoteAdapter.getItemCount(); index++) {
                    System.out.println("index: " + index );
                }
            }
        });

        cancelAttachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
