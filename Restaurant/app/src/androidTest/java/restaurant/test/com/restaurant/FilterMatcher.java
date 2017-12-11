package restaurant.test.com.restaurant;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;
import android.widget.EditText;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static org.hamcrest.Matchers.is;

/**
 * A custom matcher that checks the hint property of an {@link android.widget.EditText}. It
 * accepts either a {@link String} or a {@link org.hamcrest.Matcher}.
 */
public class FilterMatcher {

    static Matcher<View> withHint(final String substring) {
        return withHint(is(substring));
    }

    static Matcher<View> withHint(final Matcher<String> stringMatcher) {
        return new BoundedMatcher<View, EditText>(EditText.class) {

            @Override
            public boolean matchesSafely(EditText view) {
                final CharSequence hint = view.getHint();
                return hint != null && stringMatcher.matches(hint.toString());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("John");
                stringMatcher.describeTo(description);
            }
        };
    }
}