package restaurant.test.com.restaurant;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;


@RunWith(AndroidJUnit4.class)
public class CustomerActivityTest {

    @Rule
    public ActivityTestRule<CustomerActivity> mActivityTestRule = new ActivityTestRule<>(CustomerActivity.class);

    @Test
    public void customerActivityTest() {
        ViewInteraction textView = onView(
                allOf(withId(R.id.textCustomerName), withText("Marilyn Monroe"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.listView),
                                        0),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Marilyn Monroe")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.textCustomerName), withText("Marilyn Monroe"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.listView),
                                        0),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("Marilyn Monroe")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.textCustomerName), withText("Mother Teresa"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.listView),
                                        2),
                                0),
                        isDisplayed()));
        textView3.check(matches(withText("Mother Teresa")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.textCustomerName), withText("Martin Luther King"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.listView),
                                        4),
                                0),
                        isDisplayed()));
        textView4.check(matches(withText("Martin Luther King")));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.textCustomerName), withText("Martin Luther King"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.listView),
                                        4),
                                0),
                        isDisplayed()));
        textView5.check(matches(withText("Martin Luther King")));

    }
    @Test
    public void customerActivityTest2() {
        ViewInteraction textView = onView(
                allOf(withId(R.id.textCustomerName), withText("Winston Churchill"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.listView),
                                        6),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Winston Churchill")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.textCustomerName), withText("Martin Luther King"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.listView),
                                        4),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("Martin Luther King")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.textCustomerName), withText("Abraham Lincoln"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.listView),
                                        1),
                                0),
                        isDisplayed()));
        textView3.check(matches(withText("Abraham Lincoln")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.textCustomerName), withText("Winston Churchill"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.listView),
                                        6),
                                0),
                        isDisplayed()));
        textView4.check(matches(withText("Winston Churchill")));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
