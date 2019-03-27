package com.example.bmi


import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun mainActivityTest() {
        val massEditText = onView(withId(R.id.massEdit))
        massEditText.perform(replaceText("74"), closeSoftKeyboard())

        val heightEditText = onView(withId(R.id.heightEdit))
        heightEditText.perform(replaceText("181"), closeSoftKeyboard())


        val appCompatButton = onView( withId(R.id.countBmi))
        appCompatButton.perform(click())

        val textView = onView(
            withId(R.id.bmiNumberView))
        textView.check(matches(withText("22.58")))
    }
}
