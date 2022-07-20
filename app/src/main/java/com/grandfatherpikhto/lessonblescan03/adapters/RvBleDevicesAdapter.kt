package com.grandfatherpikhto.lessonblescan03.adapters

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lessonblescan03.R
import com.grandfatherpikhto.lessonblescan03.helper.clickHandler
import com.grandfatherpikhto.lessonblescan03.helper.longClickHandler
import com.grandfatherpikhto.blin.BleDevice

class RvBleDevicesAdapter : RecyclerView.Adapter<RvBleDeviceHolder>() {
    private val bluetoothDevices = mutableListOf<BluetoothDevice>()

    private var clickItemListener: RvClickItemListener<BluetoothDevice>? = null

    private var handlerClick: clickHandler<BluetoothDevice>? = null
    private var handlerLongClick: longClickHandler<BluetoothDevice>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvBleDeviceHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.ble_device_layout, parent, false)
        return RvBleDeviceHolder(view)
    }

    override fun onBindViewHolder(holder: RvBleDeviceHolder, position: Int) {
        holder.itemView.setOnClickListener { view ->
            clickItemListener?.onClickItem(bluetoothDevices[position], view)
            handlerClick?.let { it (bluetoothDevices[position], view) }
        }

        holder.itemView.setOnLongClickListener { view ->
            clickItemListener?.onLongClickItem(bluetoothDevices[position], view)
            handlerLongClick?.let { it (bluetoothDevices[position], view) }
            true
        }
        holder.bind(bluetoothDevices[position])
    }

    override fun getItemCount(): Int = bluetoothDevices.size

    fun addItem(bluetoothDevice: BluetoothDevice) {
        bluetoothDevices.add(bluetoothDevice)
        notifyItemInserted(bluetoothDevices.indexOf(bluetoothDevice))
    }

    fun addItems(devices: List<BluetoothDevice>) {
        val from = bluetoothDevices.size - 1
        bluetoothDevices.addAll(devices)
        notifyItemRangeInserted(from, devices.size)
    }

    fun clearAll() {
        val size = bluetoothDevices.size
        bluetoothDevices.clear()
        notifyItemRangeRemoved(0, size)
    }

    fun setClickItemListener(clickItemListener: RvClickItemListener<BluetoothDevice>) {
        this.clickItemListener = clickItemListener
    }

    fun setOnClickListener(clickListener : clickHandler<BluetoothDevice>) {
        handlerClick = clickListener
    }

    fun setOnLongClickListener (longClickListener : longClickHandler<BluetoothDevice>) {
        handlerLongClick = longClickListener
    }
}