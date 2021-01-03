package sandy.android.assistant;

import android.os.Build;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.test.core.app.ApplicationProvider;

import com.github.irshulx.Editor;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import java.util.ArrayList;

import sandy.android.assistant.Controller.MainActivity;
import sandy.android.assistant.Controller.NoteEditorActivity;
import sandy.android.assistant.Model.DatabaseManagement;
import sandy.android.assistant.Model.Note;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.O_MR1})

public class TC1_ManageNotes_UnitTest {

    DatabaseManagement db;

    @Before
    public void setUp() throws Exception {
        db = new DatabaseManagement(ApplicationProvider.getApplicationContext());
        Assert.assertNotNull(db);
    }

    @Test
    public void clickingSaveButton_shouldSaveCreatedNoteIntoDB() throws Exception {
        NoteEditorActivity noteEditorActivity = Robolectric.setupActivity(NoteEditorActivity.class);

        noteEditorActivity.editNote = null;

        Editor editor = (Editor) noteEditorActivity.findViewById(R.id.editor);
        EditText noteeditor_title_text = (EditText) noteEditorActivity.findViewById(R.id.noteeditor_title_text);
        ImageView noteSaveButton = (ImageView) noteEditorActivity.findViewById(R.id.imageView_save_note);

        String testHtmlContent = noteEditorActivity.htmlifyPlainText("testNoteContent");
        editor.render(testHtmlContent);
        noteeditor_title_text.setText("testNoteTitle");

        noteSaveButton.performClick();

        ArrayList<Note> notesFromDB = new ArrayList<Note>();

        notesFromDB = db.getAllNotes();

        Assert.assertEquals("testNoteTitle", notesFromDB.get(0).getTitle());
        //Assert.assertEquals(testHtmlContent, notesFromDB.get(0).getContent());

        db.deleteNote(notesFromDB.get(0));

        notesFromDB = db.getAllNotes();

        Assert.assertTrue(notesFromDB.size() == 0);

        db.close();

        noteEditorActivity.finish();
    }


    @Test
    public void clickingSaveButton_shouldUpdateNoteAtDB() throws Exception {
        Note n = new Note("title", "content", null, "date");

        db.insertNote(n);

        ArrayList<Note> notesFromDB = new ArrayList<Note>();

        notesFromDB = db.getAllNotes();

        Note testNewNote = new Note("new_title", "new_content", null, "new_date");

        NoteEditorActivity noteEditorActivity = Robolectric.setupActivity(NoteEditorActivity.class);

        noteEditorActivity.editNote = notesFromDB.get(0);

        Editor editor = (Editor) noteEditorActivity.findViewById(R.id.editor);
        EditText noteeditor_title_text = (EditText) noteEditorActivity.findViewById(R.id.noteeditor_title_text);
        ImageView noteSaveButton = (ImageView) noteEditorActivity.findViewById(R.id.imageView_save_note);

        String testHtmlContent = noteEditorActivity.htmlifyPlainText(testNewNote.getContent());
        editor.render(testHtmlContent);
        noteeditor_title_text.setText(testNewNote.getTitle());

        noteSaveButton.performClick();

        notesFromDB = db.getAllNotes();

        Assert.assertEquals(testNewNote.getTitle(), notesFromDB.get(0).getTitle());

        db.deleteNote(notesFromDB.get(0));

        notesFromDB = db.getAllNotes();

        Assert.assertTrue(notesFromDB.size() == 0);

        db.close();

        noteEditorActivity.finish();

    }

    @After
    public void closeDatabase() throws Exception {
        db.close();
    }

}