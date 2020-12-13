package sandy.android.assistant;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.irshulx.Editor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class NotebookActivity extends AppCompatActivity {

    DatabaseManagement db;
    LinearLayoutManager linearLayoutManager;
    RecyclerView listOfNotesOfNotebook;
    ArrayList<Notebook> notebookArrayList;
    ArrayList<Note> notebookNotes;
    NotebookAdapter notebookAdapter;

    ConstraintLayout notebookEditorViewConstraintLayout;
    Editor notebookEditorView;
    ImageView backButton;

    FloatingActionButton fabAddNewNoteToNotebook;
    Spinner spinnerSetNotebookViewType;

    String selectedNotebookIdString;
    Notebook selectedNotebook;

    String notebookEditorViewHtmlString;
    TextView notebookTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notebook_recycler_view);

        db = new DatabaseManagement(this);

        fabAddNewNoteToNotebook = findViewById(R.id.fabAddNewNoteToNotebook);
        notebookTitle = findViewById(R.id.textviewNotebookViewNotebookTitle);
        backButton = findViewById(R.id.buttonBackFromNotebookRecyclerView);

        notebookEditorViewConstraintLayout = findViewById(R.id.notebookEditorViewConstraintLayout);

        listOfNotesOfNotebook = findViewById(R.id.listOfNotesOfNotebook);

        linearLayoutManager = new LinearLayoutManager(this);        //defines LinearLayoutManager for vertical Recycleview orientation
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listOfNotesOfNotebook.setLayoutManager(linearLayoutManager);

        selectedNotebookIdString = getIntent().getStringExtra("NOTEBOOK_ID");
        Integer selectedNotebookId = Integer.parseInt(selectedNotebookIdString);

        selectedNotebook = db.getNotebookFromNotebookId(selectedNotebookId);
        this.setNotebookViewContent(selectedNotebook);



        spinnerSetNotebookViewType = findViewById(R.id.spinnerSetNotebookViewType);
        spinnerSetNotebookViewType.setVisibility(View.VISIBLE);

        spinnerSetNotebookViewType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals(R.string.spinner_item_1)) {         //if list view is selected,
                    if (notebookEditorViewConstraintLayout.getVisibility() == View.VISIBLE) {
                        notebookEditorViewConstraintLayout.setVisibility(View.INVISIBLE);
                        fabAddNewNoteToNotebook.setVisibility(View.VISIBLE);
                        listOfNotesOfNotebook.setVisibility(View.VISIBLE);
                    }
                }
                else if (selectedItem.equals(R.string.spinner_item_2)) {        //if note editor view is selected
                    notebookEditorView = findViewById(R.id.notebookEditorView);
                    ArrayList<String> notebookEditorViewFragments = new ArrayList<String>();
                    ArrayList<Note> notesFromNotebook = new ArrayList<Note>();
                    notesFromNotebook = db.getNotesFromNotebook(selectedNotebook);

                    if (notesFromNotebook.size() > 0) {     //if notebook has notes in it
                        for (int index = 0; index < notesFromNotebook.size(); index++) {
                            notebookEditorViewFragments.add(notesFromNotebook.get(index).getContent());
                            String lineDivider = "<hr data-tag=" + '"' + "hr" + '"' + "/>";     //insert line divider between adjacent notes.
                            if (index < notesFromNotebook.size()-1) {           //don't insert a line divider after last note.
                                notebookEditorViewFragments.add(lineDivider);
                            }
                        }

                        for (int index = 0; index < notebookEditorViewFragments.size(); index++) {      //concatenate all html strings to build a notebook view inside editor.
                            notebookEditorViewHtmlString = notebookEditorViewHtmlString + notebookEditorViewFragments.get(index);
                        }
                        fabAddNewNoteToNotebook.setVisibility(View.INVISIBLE);
                        listOfNotesOfNotebook.setVisibility(View.INVISIBLE);
                        notebookEditorViewConstraintLayout.setVisibility(View.VISIBLE);
                        notebookEditorView.render(notebookEditorViewHtmlString);        //render all concatenated html strings as editor content.
                    }
                    else {
                        System.out.println("Notebook has no notes inside");
                        //notebook has no notes in it.
                    }


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        fabAddNewNoteToNotebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo here comes the initialization of note selection screen that will be added to notebook
                Intent intent = new Intent(getApplicationContext(), NotebookAttachNoteActivity.class);
                startActivity(intent);

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        selectedNotebookIdString = getIntent().getStringExtra("NOTEBOOK_ID");
        Integer selectedNotebookId = Integer.parseInt(selectedNotebookIdString);

        selectedNotebook = db.getNotebookFromNotebookId(selectedNotebookId);
        this.setNotebookViewContent(selectedNotebook);

    }

    public void setNotebookViewContent(Notebook n) {
        notebookNotes = db.getNotesFromNotebook(n);
        notebookAdapter = new NotebookAdapter(this, notebookNotes, db);
        listOfNotesOfNotebook.setAdapter(notebookAdapter);
        notebookTitle.setText(n.getTitle());
    }
}
