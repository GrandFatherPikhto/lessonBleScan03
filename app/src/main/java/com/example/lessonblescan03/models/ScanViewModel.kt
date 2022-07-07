package com.example.lessonblescan03.models

import android.bluetooth.BluetoothDevice
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grandfatherpikhto.blin.BleApplication
import com.grandfatherpikhto.blin.BleDevice
import com.grandfatherpikhto.blin.BleScanManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class ScanViewModel : ViewModel() {
    companion object {
        private const val TAG="ScanViewModel"
    }

    private val mutableStateFlowDevice = MutableStateFlow<BluetoothDevice?>(null)
    val stateFlowDevice get() = mutableStateFlowDevice.asStateFlow()
    val valueDevice get() = stateFlowDevice.value

    private val mutableStateFlowScanning = MutableStateFlow(false)
    val stateFlowScanning get() = mutableStateFlowScanning.asStateFlow()
    val valueScanning get() = mutableStateFlowScanning.value

    val bleDevices = mutableListOf<BluetoothDevice>()

    init {
        viewModelScope.launch {
            BleScanManager.stateFlowDevice.filterNotNull().collect { device ->
                if (!bleDevices.contains(device)) {
                    bleDevices.add(device)
                    mutableStateFlowDevice.tryEmit(device)
                }
            }
        }

        viewModelScope.launch {
            BleScanManager.stateFlowScanning.collect { scanning ->
                mutableStateFlowScanning.tryEmit(scanning)
            }
        }
    }
}