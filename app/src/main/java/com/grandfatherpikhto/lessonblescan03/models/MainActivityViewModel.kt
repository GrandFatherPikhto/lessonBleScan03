package com.grandfatherpikhto.lessonblescan03.models

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainActivityViewModel : ViewModel() {
    private val msfCurrentDevice = MutableStateFlow<BluetoothDevice?>(null)
    val sfCurrentDevice get() = msfCurrentDevice.asStateFlow()
    val currentDevice get() = msfCurrentDevice.value

    fun changeCurrentDevice(bluetoothDevice: BluetoothDevice?) {
        msfCurrentDevice.tryEmit(bluetoothDevice)
    }
}