package com.grandfatherpikhto.lessonblescan03

import android.bluetooth.BluetoothDevice
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.lessonblescan03.R
import com.grandfatherpikhto.blin.BleScanManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get: Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    private var applicationContext: Context? = null

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(ScanIdling)
        applicationContext = ApplicationProvider.getApplicationContext()
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(ScanIdling)
    }

    @Test(timeout = 10000)
    fun scanTwoDevicesAndClickDevice() {
        val devices = mutableListOf<BluetoothDevice>()
        CoroutineScope(Dispatchers.IO).launch {
            BleScanManager.stateFlowDevice.filterNotNull().collect { device ->
                devices.add(device)
                if (devices.size >= 2) {
                    ScanIdling.scanned = true
                }
            }
        }
        onView(withId(R.id.action_scan)).perform(click())
        ScanIdling.scanned = false
        onView(withId(R.id.rvBleDevices)).check(matches(isDisplayed()))
        assertEquals(2, devices.size)
        onView(withText(devices[1].address)).perform(click())
        onView(withId(R.id.clDeviceInfo)).check(matches(isDisplayed()))
        onView(withId(R.id.tvDeviceAddress)).check(matches(withText(devices[1].address)))
        onView(withId(R.id.tvDeviceName))
            .check(matches(withText(
                devices[1].name ?: applicationContext
                ?.getString(R.string.ble_default_device_name))))
    }
}