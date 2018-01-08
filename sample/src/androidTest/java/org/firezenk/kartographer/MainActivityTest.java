package org.firezenk.kartographer;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
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
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 08/01/18.
 * Copyright © Jorge Garrido Oval 2017
 */
@LargeTest @RunWith(AndroidJUnit4.class) public class MainActivityTest {

    private ViewInteraction tab1;
    private ViewInteraction tab2;

    @Rule public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest2() {
        prepareTabs();

        ViewInteraction target1 = onView(allOf(childAtPosition(
                allOf(withId(R.id.viewHolder), childAtPosition(withClassName(is("android.widget.RelativeLayout")), 0)),
                0), isDisplayed()));

        onView(allOf(withText("PAGE1 -> 1"), isDisplayed()));

        target1.perform(click());

        onView(allOf(withText("PAGE1 -> 1 -> 2"), isDisplayed()));

        target1.perform(click());

        onView(allOf(withText("PAGE1 -> 1 -> 2 -> 3"), isDisplayed()));

        target1.perform(click());

        onView(allOf(withText("PAGE1 -> 1 -> 2 -> 3 -> 4"), isDisplayed()));

        tab2.perform(click());

        onView(allOf(withText("PAGE2: 10"), isDisplayed()));

        ViewInteraction target2 = onView(allOf(withId(R.id.text2), childAtPosition(
                allOf(withId(R.id.viewHolder), childAtPosition(withClassName(is("android.widget.RelativeLayout")), 0)),
                0), isDisplayed()));

        onView(allOf(withText("PAGE2: 10"), isDisplayed()));

        target2.perform(click());

        onView(allOf(withText("PAGE2: 20"), isDisplayed()));

        target2.perform(click());

        onView(allOf(withText("PAGE2: 30"), isDisplayed()));

        pressBack();

        onView(allOf(withText("PAGE2: 20"), isDisplayed()));

        tab1.perform(click());

        onView(allOf(withText("PAGE1 -> 1 -> 2 -> 3 -> 4"), isDisplayed()));

        pressBack();

        onView(allOf(withText("PAGE1 -> 1 -> 2 -> 3"), isDisplayed()));

        pressBack();

        onView(allOf(withText("PAGE1 -> 1 -> 2"), isDisplayed()));

        tab2.perform(click());

        onView(allOf(withText("PAGE2: 20"), isDisplayed()));

        pressBack();

        onView(allOf(withText("PAGE2: 10"), isDisplayed()));
    }

    private void prepareTabs() {
        tab1 = onView(
                allOf(withId(R.id.bottom_action_page1), childAtPosition(childAtPosition(withId(R.id.navigation), 0), 0),
                      isDisplayed()));
        tab2 = onView(
                allOf(withId(R.id.bottom_action_page2), childAtPosition(childAtPosition(withId(R.id.navigation), 0), 1),
                      isDisplayed()));
    }

    private static Matcher<View> childAtPosition(final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent) && view.equals(
                        ((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
