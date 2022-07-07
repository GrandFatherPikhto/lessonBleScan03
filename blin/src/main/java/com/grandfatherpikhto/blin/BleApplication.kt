package com.grandfatherpikhto.blin

import android.app.Application
import android.util.Log

class BleApplication : Application() {
    companion object {
        private const val TAG="BleApplication"
    }
    var bleScanManager:BleScanManager? = null

    override fun onCreate() {
        Log.d(TAG, "onCreate()")
        super.onCreate()
    }
}