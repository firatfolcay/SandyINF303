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
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DeleteNotificationTest {
    String TEST_TITLE = "notification test";

    @Test
    public void test1_deleteNotification(){
        ActivityScenario activityScenario = ActivityScenario.launch(MainActivity.class);

        // click notifications
        onView(withId(R.id.showNotification)).perform(click());

        // delete notification
        onView(allOf(withId(R.id.deleteNotification), withParent(hasSibling(hasDescendant(withText(TEST_TITLE)))))).perform(click());
    }

    @Test
    public void test2_checkNotification(){
        ActivityScenario activityScenario = ActivityScenario.launch(MainActivity.class);

        // click notifications
        onView(withId(R.id.showNotification)).perform(click());

        // check that does not exist
        onView(hasDescendant(withText(TEST_TITLE))).check(doesNotExist());

    }
}
