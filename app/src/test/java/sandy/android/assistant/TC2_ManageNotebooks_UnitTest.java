package sandy.android.assistant;

import android.os.Build;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.test.core.app.ApplicationProvider;

import com.github.irshulx.Editor;

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
import sandy.android.assistant.Model.Notebook;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.O_MR1})

public class TC2_ManageNotebooks_UnitTest {

    DatabaseManagement db;

    @Before
    public void setUp() throws Exception {
        db = new DatabaseManagement(ApplicationProvider.getApplicationContext());
        Assert.assertNotNull(db);
    }

    @Test
    public void clickingCreateButton_shouldSaveCreatedNotebookIntoDB() throws Exception {
        /*MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.findViewById(R.id.buttonMainActivityNavigationDrawer).performClick();
        mainActivity.findViewById(R.id.buttonAddNotebook).performClick();
        Thread.sleep(1000);
        mainActivity.notebookPopupEditText.setText("notebook");*/

        Notebook notebook = new Notebook("test");
        db.insertNotebook(notebook);

        ArrayList<Notebook> notebooksFromDB = new ArrayList<Notebook>();

        notebooksFromDB = db.getAllNotebooks();

        Assert.assertEquals("test", notebooksFromDB.get(0).getTitle());

        db.deleteNotebook(notebooksFromDB.get(0));

        notebooksFromDB = db.getAllNotebooks();

        Assert.assertTrue(notebooksFromDB.size() == 0);

    }

}