package sandy.android.assistant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.irshulx.Editor;

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

    Notebook selectedNotebook;

    String notebookEditorViewHtmlString;
    TextView notebookTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notebook_attach_note_view);

        db = new DatabaseManagement(this);

        Bundle b = getIntent().getExtras();
        if(b != null){
            if(b.get("NOTEBOOK_ID") != null){
                selectedNotebook = db.getNotebookFromNotebookId(b.getInt("NOTEBOOK_ID"));
            }
            else{
                finish();
                Toast.makeText(getApplicationContext(), "Operation failed", Toast.LENGTH_LONG).show();
                return;
            }
        }
        else{
            finish();
            Toast.makeText(getApplicationContext(), "Operation failed", Toast.LENGTH_LONG).show();
            return;
        }

        listOfNotesToAttach = findViewById(R.id.listOfNotesToAttach);
        attachSelectedNotesButton = findViewById(R.id.buttonAttachSelectedNotes);
        cancelAttachButton = findViewById(R.id.buttonCancelAttach);

        notesWithNoNotebookAttached = db.getAllNotesExceptCurrentNotebook(selectedNotebook);
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
