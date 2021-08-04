package com.example.app_tarefas_diarias

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.example.app_tarefas_diarias.activitys.ActivityTarefa
import com.example.app_tarefas_diarias.activitys.SplashActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@Suppress("DEPRECATION")
@RunWith(AndroidJUnit4::class)
class SplashActivityTest {

    @Rule
    val rule = ActivityTestRule(SplashActivity::class.java)

    @Test
    fun whenOpenSplashActivity_ShowLogo(){
        onView(withId(R.id.image_splash)).check(matches(isDisplayed()))
    }
}