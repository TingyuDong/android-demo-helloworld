package com.thoughtworks.androidtrain

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {
    @get:Rule
    val activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    fun should_be_checked_if_click_the_check_box() {
        onView(withId(R.id.pick_login)).perform(click())
        onView(withId(R.id.checkbox)).check(matches(isDisplayed()))
        onView(withId(R.id.checkbox)).perform(click())
        onView(withId(R.id.checkbox)).check(matches(isChecked()))
    }
}