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
 * Created by Jorge Garrido Oval, aka firezenk on 04/12/17.
 * Copyright © Jorge Garrido Oval 2017
 */
@LargeTest @RunWith(AndroidJUnit4.class) public class MainActivityTest {

    @Rule public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test public void mainActivityTest() {
        ViewInteraction leftView = onView(allOf(childAtPosition(allOf(withId(R.id.leftPlaceholder), childAtPosition(
                withClassName(is("android.widget.LinearLayout")), 0)), 0), isDisplayed()));

        ViewInteraction rightView = onView(allOf(childAtPosition(allOf(withId(R.id.rightPlaceholder), childAtPosition(
                withClassName(is("android.widget.LinearLayout")), 1)), 0), isDisplayed()));

        ViewInteraction leftBack = onView(allOf(withId(R.id.backLeft), withText("Back on LEFT"), childAtPosition(
                allOf(withId(R.id.buttons), childAtPosition(withClassName(is("android.widget.RelativeLayout")), 1)), 0),
                                                isDisplayed()));

        ViewInteraction rightBack = onView(allOf(withId(R.id.backRight), withText("Back on RIGHT"), childAtPosition(
                allOf(withId(R.id.buttons), childAtPosition(withClassName(is("android.widget.RelativeLayout")), 1)), 1),
                                                 isDisplayed()));

        onView(allOf(withText("100"), isDisplayed()));
        onView(allOf(withText("200"), isDisplayed()));

        leftView.perform(click());

        onView(allOf(withText("101"), isDisplayed()));

        rightView.perform(click());
        rightView.perform(click());

        onView(allOf(withText("202"), isDisplayed()));

        rightBack.perform(click());

        onView(allOf(withText("201"), isDisplayed()));

        leftBack.perform(click());

        onView(allOf(withText("100"), isDisplayed()));

        rightBack.perform(click());

        onView(allOf(withText("200"), isDisplayed()));
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
