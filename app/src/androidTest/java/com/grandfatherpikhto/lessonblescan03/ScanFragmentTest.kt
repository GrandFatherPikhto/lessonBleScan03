package com.grandfatherpikhto.lessonblescan03

import android.bluetooth.BluetoothDevice
import android.util.Log
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

import com.example.lessonblescan03.R
import com.grandfatherpikhto.blin.BleScanManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest

@RunWith(AndroidJUnit4::class)
@LargeTest
class ScanFragmentTest {
    val ADDRESS="01:02:03:04:05:06"
    val NAME="TEST_DEVICE"

    private var idlingResource: IdlingResource? = null

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)


    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
        activityRule.scenario.close()
    }

    @Test(timeout = 10000)
    fun testMainActivity() {
        activityRule.scenario.onActivity { _ ->
            runBlocking {

            }
        }
        onView(withId(R.id.action_scan)).perform(click())
    }

    @Test
    fun testFragmentScan() {
        // val scenario = launchFragmentInContainer<ScanFragment>()
    }
}
