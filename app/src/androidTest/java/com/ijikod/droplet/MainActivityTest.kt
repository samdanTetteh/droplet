package com.ijikod.droplet

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test

class MainActivityTest {

    @Rule
    @JvmField
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testEmailField() {
        fillField(R.id.email_txt, "samdan@email.com")
    }

    @Test
    fun testFirstNameField() {
        fillField(R.id.first_name_txt, "Samdan")
    }

    @Test
    fun testLastNameField() {
        fillField(R.id.last_name_txt, "Tetteh")
    }

    private fun fillField(@IdRes id: Int, text: String) {
        val mainActivity = activityRule.activity as MainActivity
        Espresso.onView(ViewMatchers.withId(id))
            .inRoot(RootMatchers.withDecorView(Matchers.`is`(mainActivity.window.decorView)))
            .perform(ViewActions.typeText(text))
    }

}