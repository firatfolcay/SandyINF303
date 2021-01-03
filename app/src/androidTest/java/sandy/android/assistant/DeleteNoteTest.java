package sandy.android.assistant;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import sandy.android.assistant.Controller.MainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DeleteNoteTest {
    String TEST_TITLE = "Title text";
    String EDIT_TITLE = "Edited title";

    //Check if MainActivity is in view
    @Test
    public void test1_isMainActivityInView() {
        ActivityScenario activityScenario = ActivityScenario.launch(MainActivity.class);
        onView(withId(R.id.mainActivityConstraintLayout)).check(matches(isDisplayed()));
    }

    //Checks if note was added
    //WILL FAIL IF THERE ARE MULTIPLE NOTES WITH EDIT TITLE
    //FIXME: Can't check body because couldn't figure out how to test the third party editor
    @Test
    public void test2_checkNote(){
        ActivityScenario activityScenario = ActivityScenario.launch(MainActivity.class);

        //Click on the note with title text
        onView(allOf(withParent(withParent(withParent(allOf(withId(R.id.cardView), withChild(withChild(withChild(allOf(withId(R.id.noteTitle), anyOf(withText(TEST_TITLE), withText(EDIT_TITLE)))))))))), withId(R.id.deleteNote))).perform(click());

        //Check if NoteEditorActivity is in view
        onView(withId(R.id.noteEditorActivityConstraintLayout));

    }
}
