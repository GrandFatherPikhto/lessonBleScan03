package com.grandfatherpikhto.blin

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import android.os.ParcelUuid

data class BleDevice(val address: String, val name: String? = null, val bondState: Int = 0, val uuids: Array<ParcelUuid> = arrayOf()) {
    @SuppressLint("MissingPermission")
    constructor(bluetoothDevice: BluetoothDevice) : this(bluetoothDevice.address,
        bluetoothDevice.name,
        bluetoothDevice.bondState,
        bluetoothDevice.uuids ?: arrayOf())
    constructor(scanResult: ScanResult) : this(bluetoothDevice = scanResult.device)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BleDevice

        if (address != other.address) return false
        if (name != other.name) return false
        if (bondState != other.bondState) return false
        if (!uuids.contentEquals(other.uuids)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = address.hashCode()
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + bondState
        result = 31 * result + uuids.contentHashCode()
        return result
    }
}
