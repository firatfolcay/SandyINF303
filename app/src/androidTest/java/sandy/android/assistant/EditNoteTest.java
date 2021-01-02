package sandy.android.assistant;

import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.CoordinatesProvider;
import androidx.test.espresso.action.GeneralClickAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Tap;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import sandy.android.assistant.Controller.MainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static android.view.KeyEvent.*;
import static sandy.android.assistant.CreateNoteTest.typeString;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EditNoteTest {
    String TEST_TITLE = "Title text";
    String TEST_BODY = "Body\nof\ntext.";
    String EDIT_TITLE = "Edited title";
    String EDIT_BODY = "Text\nwas\nedited.";

    //Check if MainActivity is in view
    @Test
    public void test1_isMainActivityInView() {
        ActivityScenario activityScenario = ActivityScenario.launch(MainActivity.class);
        onView(withId(R.id.mainActivityConstraintLayout)).check(matches(isDisplayed()));
    }

    //Edits note
    //WILL FAIL IF THERE ARE MULTIPLE NOTES WITH EDIT TITLE
    @Test
    public void test2_editNote(){
        ActivityScenario activityScenario = ActivityScenario.launch(MainActivity.class);
        try{
            Thread.sleep(1000);
        }catch(Exception e){

        }

        //Click on the note with title text
        onView(withId(R.id.listOfNotes)).perform(actionOnItem(hasDescendant(withText(TEST_TITLE)), click()));

        //Check if NoteEditorActivity is in view
        onView(withId(R.id.noteEditorActivityConstraintLayout));

        //Edit text in title
        onView(withId(R.id.noteeditor_title_text)).perform(replaceText(EDIT_TITLE));

        //Edit text in body
        //FIXME: pressing end doesn't work, cleartext sometimes doesn't either
        onView(withId(R.id.noteEditorActivityConstraintLayout)).perform(clickXY(100, 450));

        clearText(R.id.editor);
        typeString(EDIT_BODY, R.id.editor);

        //Click save note
        onView(withId(R.id.imageView_save_note)).perform(click());
    }

    //Checks if note was edited
    //WILL FAIL IF THERE ARE MULTIPLE NOTES WITH TEST TITLE
    //FIXME: Can't check body because couldn't figure out how to test the third party editor
    @Test
    public void test3_checkNote(){
        ActivityScenario activityScenario = ActivityScenario.launch(MainActivity.class);

        //Click on the note with edited title text
        onView(withId(R.id.listOfNotes)).perform(actionOnItem(hasDescendant(withText(EDIT_TITLE)), click()));

        //Check if NoteEditorActivity is in view
        onView(withId(R.id.noteEditorActivityConstraintLayout));
    }

    // https://stackoverflow.com/a/22798043
    public static ViewAction clickXY(final int x, final int y){
        return new GeneralClickAction(
                Tap.SINGLE,
                new CoordinatesProvider() {
                    @Override
                    public float[] calculateCoordinates(View view) {

                        final int[] screenPos = new int[2];
                        view.getLocationOnScreen(screenPos);

                        final float screenX = screenPos[0] + x;
                        final float screenY = screenPos[1] + y;
                        float[] coordinates = {screenX, screenY};

                        return coordinates;
                    }
                },
                Press.FINGER);
    }

    //clear text in focus
    private void clearText(int id){
        onView(withId(id)).perform(pressKey(KEYCODE_SYSTEM_NAVIGATION_DOWN));
        onView(withId(id)).perform(pressKey(KEYCODE_SYSTEM_NAVIGATION_DOWN));
        onView(withId(id)).perform(pressKey(KEYCODE_MOVE_END));
        for(int i = 0; i < 30; i++){
            onView(withId(id)).perform(pressKey(KEYCODE_DEL));
        }
    }
}
