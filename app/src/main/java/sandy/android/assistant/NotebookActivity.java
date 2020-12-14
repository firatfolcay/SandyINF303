package sandy.android.assistant;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.irshulx.Editor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class NotebookActivity extends AppCompatActivity {
    private static final int REQUEST_NOTEBOOK = 0;

    DatabaseManagement db;
    ConstraintLayout constraintLayout;
    LinearLayoutManager linearLayoutManager;
    RecyclerView listOfNotesOfNotebook;
    ArrayList<Notebook> notebookArrayList;
    ArrayList<Note> notebookNotes;
    NotebookAdapter notebookAdapter;

    ConstraintLayout notebookEditorViewConstraintLayout;
    Editor notebookEditorView;
    ImageView backButton;
    ImageView editTitleButton;

    FloatingActionButton fabAddNewNoteToNotebook;
    Spinner spinnerSetNotebookViewType;

    Notebook selectedNotebook;

    String notebookEditorViewHtmlString;
    TextView notebookTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notebook_recycler_view);

        db = new DatabaseManagement(this);
        constraintLayout = findViewById(R.id.notebookConstraintLayout);
        fabAddNewNoteToNotebook = findViewById(R.id.fabAddNewNoteToNotebook);
        notebookTitle = findViewById(R.id.textviewNotebookViewNotebookTitle);
        backButton = findViewById(R.id.buttonBackFromNotebookRecyclerView);
        editTitleButton = findViewById(R.id.imageViewTitleEditButton);

        notebookEditorViewConstraintLayout = findViewById(R.id.notebookEditorViewConstraintLayout);

        listOfNotesOfNotebook = findViewById(R.id.listOfNotesOfNotebook);

        linearLayoutManager = new LinearLayoutManager(this);        //defines LinearLayoutManager for vertical Recycleview orientation
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listOfNotesOfNotebook.setLayoutManager(linearLayoutManager);

        spinnerSetNotebookViewType = findViewById(R.id.spinnerSetNotebookViewType);
        spinnerSetNotebookViewType.setVisibility(View.VISIBLE);

        Bundle b = getIntent().getExtras();
        if(b != null){
            if(b.get("NOTEBOOK_ID") != null){
                selectedNotebook = db.getNotebookFromNotebookId(b.getInt("NOTEBOOK_ID"));
                this.setNotebookViewContent(selectedNotebook);
            }
            else{
                finish();
                Toast.makeText(getApplicationContext(), "Failed to open selected Notebook", Toast.LENGTH_LONG).show();
                return;
            }
        }
        else{
            finish();
            Toast.makeText(getApplicationContext(), "Failed to open selected Notebook", Toast.LENGTH_LONG).show();
            return;
        }

        spinnerSetNotebookViewType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals(getResources().getString(R.string.spinner_item_1))) {         //if list view is selected,
                    if (notebookEditorViewConstraintLayout.getVisibility() == View.VISIBLE) {
                        notebookEditorViewConstraintLayout.setVisibility(View.INVISIBLE);
                        fabAddNewNoteToNotebook.setVisibility(View.VISIBLE);
                        listOfNotesOfNotebook.setVisibility(View.VISIBLE);
                        constraintLayout.setBackgroundColor(getResources().getColor(R.color.darkBlue));
                    }
                }
                else if (selectedItem.equals(getResources().getString(R.string.spinner_item_2))) {        //if note editor view is selected
                    notebookEditorView = findViewById(R.id.notebookEditorView);
                    notebookEditorView.clearAllContents();
                    ArrayList<String> notebookEditorViewFragments = new ArrayList<String>();
                    ArrayList<Note> notesFromNotebook = new ArrayList<Note>();
                    notesFromNotebook = db.getNotesFromNotebook(selectedNotebook);
                    constraintLayout.setBackgroundColor(getResources().getColor(R.color.white));
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
                        notebookEditorViewHtmlString = "";
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
                //initialization of note selection screen for notebook
                Intent intent = new Intent(getApplicationContext(), NotebookAttachNoteActivity.class);
                intent.putExtra("NOTEBOOK_ID", selectedNotebook.getId());
                startActivityForResult(intent, REQUEST_NOTEBOOK);

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editTitleButton.setOnClickListener(v -> {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.notebook_edit_title_popup_view, null);

            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

            popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

            EditText notebookEditTitleEditText = (EditText) popupView.findViewById(R.id.notebookEditTitleEditText);              //initialize popup windows views
            Button notebookEditTitleSaveButton = (Button) popupView.findViewById(R.id.notebookEditTitleSaveButton);
            Button notebookEditTitleCancelButton = (Button) popupView.findViewById(R.id.notebookEditTitleCancelButton);

            notebookEditTitleSaveButton.setOnClickListener(v1 -> {
                if (!notebookEditTitleEditText.getText().toString().isEmpty()) {           //if notebook title input isn't empty
                    db.updateNotebook(new Notebook(notebookEditTitleEditText.getText().toString()),
                            selectedNotebook);
                    onResume();                             //to update the current screen
                    popupWindow.dismiss();                  //dispose popup
                }
            });

            notebookEditTitleCancelButton.setOnClickListener(v2 -> {
                popupWindow.dismiss();          //dispose popup
            });
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        selectedNotebook = db.getNotebookFromNotebookId(selectedNotebook.getId());
        this.setNotebookViewContent(selectedNotebook);
    }

    public void setNotebookViewContent(Notebook n) {
        notebookNotes = db.getNotesFromNotebook(n);
        notebookAdapter = new NotebookAdapter(this, notebookNotes, db);
        listOfNotesOfNotebook.setAdapter(notebookAdapter);
        notebookTitle.setText(n.getTitle());
    }
}
