package restaurant.test.com.restaurant;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.not;

/**
 * Tests for {@link CustomerActivity} showcasing the use of custom matchers (see
 * {@link FilterMatcher#withHint}).
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class FilterMatchersTest {

    private static final String INVALID_STRING_TO_BE_TYPED = "Marilyn Monroe";

    private static final String NAME_ENDING = "John?";

    private static final String NAME_INVALID_ENDING = "Monroe?";

    private static final String VALID_STRING_TO_BE_TYPED = "Marilyn Monroe";

    private static final String HINT_TEXT = "Type customer name";

    @Rule
    public final ActivityTestRule<CustomerActivity> mActivityRule = new ActivityTestRule<>(
            CustomerActivity.class);

    // A valid string with a valid ending
    private String mStringWithValidEnding;

    // A valid string from the search preparations
    private String mValidStringToBeTyped;


    @Before
    public void initValidStrings() {
        // Produce a string with valid ending.
        mStringWithValidEnding = "Random " + mActivityRule.getActivity().getResources().getString(R.string.filter_customer_name);
        // Get one of the available coffee preparations.
        mValidStringToBeTyped = VALID_STRING_TO_BE_TYPED;
    }

    /**
     * Uses a custom matcher {@link FilterMatcher#withHint}, with a {@link String} as the argument.
     */
    @Test
    public void hint_isDisplayedInEditText() {
        String hintText = mActivityRule.getActivity().getResources().getString(R.string.filter_customer_name);

        onView(withId(R.id.myFilter)).check(matches(FilterMatcher.withHint(hintText)));
    }

    /**
     * Same as above but using a {@link org.hamcrest.Matcher} as the argument.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void hint_endsWith() {
        // This check will probably fail if the app is localized and the language is changed. Avoid
        // string literals in code!
        onView(withId(R.id.myFilter)).check(matches(FilterMatcher.withHint(anyOf(
                endsWith(NAME_ENDING), endsWith(HINT_TEXT), endsWith(NAME_INVALID_ENDING)))));
    }

    @Test
    public void editText_canBeTypedInto() {
        onView(withId(R.id.myFilter))
                .perform(typeText(mValidStringToBeTyped), closeSoftKeyboard())
                .check(matches(withText(mValidStringToBeTyped)));
    }

    @Test
    public void validation_resultIsOneOfTheValidStrings() {
        // Type a valid string and click on the button.
        onView(withId(R.id.myFilter))
                .perform(typeText(mValidStringToBeTyped), closeSoftKeyboard());
        onView(withId(R.id.myFilter)).perform(click());

        // Check that the correct sign is displayed.
        onView(withId(R.id.myFilter)).check(matches(isDisplayed()));
        onView(withId(R.id.myFilter)).check(matches(not(isDisplayed())));
    }

    @Test
    public void validation_resultHasCorrectEnding() {
        // Type a string with a valid ending and click on the button.
        onView(withId(R.id.myFilter))
                .perform(typeText(mStringWithValidEnding), closeSoftKeyboard());
        onView(withId(R.id.myFilter)).perform(click());

        // Check that the correct sign is displayed.
        onView(withId(R.id.myFilter)).check(matches(isDisplayed()));
        onView(withId(R.id.myFilter)).check(matches(not(isDisplayed())));
    }

    @Test
    public void validation_resultIsIncorrect() {
        // Type a valid string and click on the button.
        onView(withId(R.id.myFilter))
                .perform(typeText(INVALID_STRING_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.myFilter)).perform(click());

        // Check that the correct sign is displayed.
        onView(withId(R.id.myFilter)).check(matches(isDisplayed()));
        onView(withId(R.id.myFilter)).check(matches(not(isDisplayed())));
    }
}