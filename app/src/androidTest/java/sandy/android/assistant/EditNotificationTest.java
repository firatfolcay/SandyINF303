package sandy.android.assistant;

import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.material.button.MaterialButton;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import sandy.android.assistant.Controller.MainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.PickerActions.setDate;
import static androidx.test.espresso.contrib.PickerActions.setTime;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EditNotificationTest {
    String TEST_TITLE = "notification test";

    @Test
    public void test1_editNotification(){
        ActivityScenario activityScenario = ActivityScenario.launch(MainActivity.class);

        // click notifications
        onView(withId(R.id.showNotification)).perform(click());

        // select note
        onView(withId(R.id.listOfNotifications)).perform(actionOnItem(hasDescendant(withText(TEST_TITLE)), click()));

        // edit date
        onView(withId(R.id.datePickerButton)).perform(click());

        onView(instanceOf(DatePicker.class)).perform(setDate(2022, 2,2));
        onView(allOf(instanceOf(MaterialButton.class), withText("OK"))).perform(click());

        // edit time
        onView(withId(R.id.timePickerButton)).perform(click());

        onView(instanceOf(TimePicker.class)).perform(setTime(13, 45));
        onView(allOf(instanceOf(MaterialButton.class), withText("OK"))).perform(click());

        // save notification
        onView(withId(R.id.saveNotificationButton)).perform(click());
    }

    @Test
    public void test2_checkNotification(){
        ActivityScenario activityScenario = ActivityScenario.launch(MainActivity.class);

        // click notifications
        onView(withId(R.id.showNotification)).perform(click());

        // select note
        onView(withId(R.id.listOfNotifications)).perform(actionOnItem(hasDescendant(withText(TEST_TITLE)), click()));

        // check date
        onView(withId(R.id.datePickertextView)).check(matches(withText("Date: 2-2-2022")));

        // check time
        onView(withId(R.id.timePickertextView)).check(matches(withText("Hour: 13 Minute: 45")));
    }
}
