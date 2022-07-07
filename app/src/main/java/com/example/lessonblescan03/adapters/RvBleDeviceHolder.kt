package com.example.lessonblescan03.adapters

import android.bluetooth.BluetoothDevice
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.lessonblescan03.R
import com.example.lessonblescan03.databinding.BleDeviceLayoutBinding
import com.grandfatherpikhto.blin.BleDevice

class RvBleDeviceHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    private val binding = BleDeviceLayoutBinding.bind(view)

    fun bind(bluetoothDevices: BluetoothDevice) {
        binding.apply {
            tvName.text = bluetoothDevices.name ?: view.context.getString(R.string.ble_default_device_name)
            tvAddress.text = bluetoothDevices.address
            if (bluetoothDevices.bondState == BluetoothDevice.BOND_BONDED) {
                ivPaired.setImageResource(R.drawable.ic_baseline_bluetooth_connected_48)
            } else {
                ivPaired.setImageResource(R.drawable.ic_baseline_bluetooth_48)
            }
        }
    }
}
