package sandy.android.assistant;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import sandy.android.assistant.Controller.MainActivity;
import sandy.android.assistant.Controller.NoteEditorActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static sandy.android.assistant.CreateNoteTest.clickXY;
import static sandy.android.assistant.CreateNoteTest.typeString;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EditNotebookTest {
    String NOTEBOOK_TITLE = "notebook1";
    String TEST_TITLE1 = "test1";
    String TEST_TITLE2 = "test2";
    String TEST_BODY = "Body\nof\ntext.";

    @Test
    public void test1_addNewNote() {
        ActivityScenario activityScenario = ActivityScenario.launch(NoteEditorActivity.class);

        //Type text in title
        onView(withId(R.id.noteeditor_title_text)).perform(click()).perform(typeText(TEST_TITLE1));

        //Type text in body
        pressBack();

        onView(withId(R.id.noteEditorActivityConstraintLayout)).perform(clickXY(100, 450));

        typeString(TEST_BODY, R.id.editor);

        //Click save note
        onView(withId(R.id.imageView_save_note)).perform(click());

        ///////

        activityScenario = ActivityScenario.launch(NoteEditorActivity.class);

        //Type text in title
        onView(withId(R.id.noteeditor_title_text)).perform(click()).perform(typeText(TEST_TITLE2));

        //Type text in body
        pressBack();

        onView(withId(R.id.noteEditorActivityConstraintLayout)).perform(clickXY(100, 450));

        typeString(TEST_BODY, R.id.editor);

        //Click save note
        onView(withId(R.id.imageView_save_note)).perform(click());
    }

    @Test
    public void test2_attachNotes(){
        ActivityScenario activityScenario = ActivityScenario.launch(MainActivity.class);

        // check if main activity is displayed
        onView(withId(R.id.mainActivityConstraintLayout)).check(matches(isDisplayed()));

        // click notebook drawer
        onView(withId(R.id.buttonMainActivityNavigationDrawer)).perform(click());

        // select notebook
        onView(allOf(withId(R.id.cardView), hasDescendant(allOf(withId(R.id.notebookTitle), withText(NOTEBOOK_TITLE))))).perform(click());

        // click attach note
        onView(withId(R.id.fabAddNewNoteToNotebook)).perform(click());
        // check note1
        onView(allOf(withId(R.id.noteToAttachCheckBox), withParent(hasSibling(hasDescendant(allOf(withId(R.id.noteToAttachTitle), withText(TEST_TITLE1))))))).perform(click());
        // check note2
        onView(allOf(withId(R.id.noteToAttachCheckBox), withParent(hasSibling(hasDescendant(allOf(withId(R.id.noteToAttachTitle), withText(TEST_TITLE2))))))).perform(click());
        // click attach
        onView(withId(R.id.buttonAttachSelectedNotes)).perform(click());
    }

    @Test
    public void test3_checkAttached(){
        ActivityScenario activityScenario = ActivityScenario.launch(MainActivity.class);

        // check if main activity is displayed
        onView(withId(R.id.mainActivityConstraintLayout)).check(matches(isDisplayed()));

        // click notebook drawer
        onView(withId(R.id.buttonMainActivityNavigationDrawer)).perform(click());

        // select notebook
        onView(allOf(withId(R.id.cardView), hasDescendant(allOf(withId(R.id.notebookTitle), withText(NOTEBOOK_TITLE))))).perform(click());

        // check note1
        onView(allOf(withId(R.id.noteOfNotebookTitle), withText(TEST_TITLE1))).check(matches(isDisplayed()));

        // check note2
        onView(allOf(withId(R.id.noteOfNotebookTitle), withText(TEST_TITLE2))).check(matches(isDisplayed()));
    }

    @Test
    public void test4_removeNote(){
        ActivityScenario activityScenario = ActivityScenario.launch(MainActivity.class);

        // check if main activity is displayed
        onView(withId(R.id.mainActivityConstraintLayout)).check(matches(isDisplayed()));

        // click notebook drawer
        onView(withId(R.id.buttonMainActivityNavigationDrawer)).perform(click());

        // select notebook
        onView(allOf(withId(R.id.cardView), hasDescendant(allOf(withId(R.id.notebookTitle), withText(NOTEBOOK_TITLE))))).perform(click());

        // remove note 1
        onView(allOf(withId(R.id.notesOfNotebookDelete), withParent(hasSibling(hasDescendant(allOf(withId(R.id.noteOfNotebookTitle), withText(TEST_TITLE1))))))).perform(click());
    }

    @Test
    public void test5_checkNoteRemoved(){
        ActivityScenario activityScenario = ActivityScenario.launch(MainActivity.class);

        // check if main activity is displayed
        onView(withId(R.id.mainActivityConstraintLayout)).check(matches(isDisplayed()));

        // click notebook drawer
        onView(withId(R.id.buttonMainActivityNavigationDrawer)).perform(click());

        // select notebook
        onView(allOf(withId(R.id.cardView), hasDescendant(allOf(withId(R.id.notebookTitle), withText(NOTEBOOK_TITLE))))).perform(click());

        onView(allOf(withId(R.id.noteOfNotebookTitle), withText(TEST_TITLE1))).check(doesNotExist());
    }
}
