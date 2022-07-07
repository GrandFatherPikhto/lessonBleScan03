package com.grandfatherpikhto.blin

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanResult
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@RequiresApi(Build.VERSION_CODES.O)
class BleScanReceiver : BroadcastReceiver() {
    companion object Receiver {
        private const val TAG="BleScanReceiver"
        private const val ACTION_BLE_SCAN = "com.rqd.testscanbt.ACTION_BLE_SCAN"
        private fun newIntent(context: Context): Intent {
            Log.e(TAG, "newIntent()")
            val intent = Intent(
                context,
                BleScanReceiver::class.java
            )
            intent.action = ACTION_BLE_SCAN
            return intent
        }

        @SuppressLint("UnspecifiedImmutableFlag")
        fun getBroadcast(context: Context, requestCode: Int): PendingIntent {
            Log.e(TAG, "getBroadcast()")
            return PendingIntent.getBroadcast(
                context,
                requestCode,
                newIntent(context),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }

    private var bleScanManager:BleScanManager? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        if ( context != null && intent != null ) {
            if (context.applicationContext is BleApplication) {
                bleScanManager = (context.applicationContext as BleApplication).bleScanManager
            }

            when (intent.action) {
                ACTION_BLE_SCAN -> {
                    bleScanManager?.onScanReceived(intent)
                }
                else -> {
                    Log.d(TAG, "Action: ${intent.action}")
                }
            }
        }
    }
}