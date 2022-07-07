package com.grandfatherpikhto.blin

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.BluetoothLeScanner
import android.content.Context
import android.content.Intent
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanRecord
import android.os.ParcelUuid
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.kotlin.mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class BleScanManagerTest {
    companion object {
        private const val TAG:String="BleScanManagerTest"

        private const val ADDRESS_01 = "00:01:02:03:04:05"
        private const val ADDRESS_02 = "01:02:03:04:05:06"
        private const val ADDRESS_03 = "02:03:04:05:06:07"

        private const val NAME_01 = "BLE_01"
        private const val NAME_02 = "BLE_02"
        private const val NAME_03 = "BLE_03"
    }

    private val bluetoothAdapter:BluetoothAdapter by lazy {
        (RuntimeEnvironment.getApplication().applicationContext
            .getSystemService(Context.BLUETOOTH_SERVICE)
            as BluetoothManager).adapter
    }

    private val bleScanManager:BleScanManager by lazy {
        BleScanManager(context = RuntimeEnvironment.getApplication().applicationContext,
            UnconfinedTestDispatcher())
    }

    private fun mockBluetoothDevice (address: String? = null,
                                     name: String? = null,
                                     uuids: List<ParcelUuid>? = null
    ) : BluetoothDevice = bluetoothAdapter.getRemoteDevice(address ?:
        Random.nextBytes(6).joinToString (":") { String.format("%02X", it) })
            .let { device ->
                if (name != null ) shadowOf(device).setName(name)
                if (!uuids.isNullOrEmpty()) shadowOf(device).setUuids(uuids.toTypedArray())
                device
            }

    private fun mockIntentScanResult (devices: List<BluetoothDevice>
        ) : Intent = Intent(RuntimeEnvironment.getApplication().applicationContext,
                                ScanResult::class.java).let { intent ->
        val scanResults = mutableListOf<ScanResult>()
        devices.forEach { device ->
            ScanResult(device,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                mock<ScanRecord>(),
                0).let { scanResult ->
                scanResults.add(scanResult)
            }
            intent.putParcelableArrayListExtra(BluetoothLeScanner.EXTRA_LIST_SCAN_RESULT,
                scanResults.toCollection(ArrayList<ScanResult>()))
        }
        intent
    }

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun testScan() = runTest(UnconfinedTestDispatcher()) {
        val bluetoothDevices = mutableListOf<BluetoothDevice>()
        val intent = mockIntentScanResult(List<BluetoothDevice>(7) {
            val bluetoothDevice = mockBluetoothDevice()
            bluetoothDevices.add(bluetoothDevice)
            bluetoothDevice
        })
        bleScanManager.startScan()
        assertNotNull((RuntimeEnvironment.getApplication() as BleApplication).bleScanManager)
        bleScanManager.onScanReceived(intent)
        assertTrue(BleScanManager.valueScanning)
        assertNotNull(BleScanManager.valueDevice)
        assertEquals(bluetoothDevices.last().address, BleScanManager.valueDevice!!.address)
        bleScanManager.stopScan()
    }

    @Test
    fun repeatCheck() = runTest(UnconfinedTestDispatcher()) {
        val bluetoothDevices = listOf<BluetoothDevice>(
            mockBluetoothDevice(name = NAME_01, address = ADDRESS_01),
            mockBluetoothDevice(name = NAME_01, address = ADDRESS_01),
            mockBluetoothDevice(name = NAME_02, address = ADDRESS_02),
            mockBluetoothDevice(name = NAME_01, address = ADDRESS_01),
        )
        val intent = mockIntentScanResult(bluetoothDevices)
        bleScanManager.startScan()
        assertNotNull((RuntimeEnvironment.getApplication() as BleApplication).bleScanManager)
        bleScanManager.onScanReceived(intent)
        assertTrue(BleScanManager.valueScanning)
        assertNotNull(BleScanManager.valueDevice)
        assertEquals(ADDRESS_02, BleScanManager.valueDevice!!.address)
        bleScanManager.stopScan()
    }
}