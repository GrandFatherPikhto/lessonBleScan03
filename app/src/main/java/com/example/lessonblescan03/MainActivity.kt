package com.example.lessonblescan03

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.lessonblescan03.databinding.ActivityMainBinding
import com.example.lessonblescan03.models.MainActivityViewModel
import com.example.lessonblescan03.models.ScanViewModel
import com.grandfatherpikhto.blin.BleApplication
import com.grandfatherpikhto.blin.BleScanManager
import com.grandfatherpikhto.blin.RequestPermissions
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val TAG="MainActivity"
    }

    private val requestPermissions: RequestPermissions by lazy {
        RequestPermissions(this).let {
            lifecycle.addObserver(it)
            it
        }
    }

    private val bleScanManager by lazy {
        BleScanManager(this)
    }

    private val scanViewModel by viewModels<ScanViewModel>()
    private val mainActivityViewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        lifecycleScope.launch {
            requestPermissions.stateFlowRequestPermission.filterNotNull().collect { permission ->
                Log.e(TAG, "Permission ${permission.permission}, ${permission.granted}")
                if (permission.granted) {
                    Toast.makeText(baseContext, getString(R.string.message_permission_granted, permission.permission), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(baseContext, getString(R.string.message_permission_not_granted, permission.permission), Toast.LENGTH_SHORT).show()
                    finishAndRemoveTask()
                    exitProcess(0)
                }
            }
        }

        requestPermissions.requestPermissions(listOf(
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION",
        ))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        menu.findItem(R.id.action_scan)?.let { actionScan ->
            lifecycleScope.launch {
                scanViewModel.stateFlowScanning.collect { scanning ->
                    setScanTitle(actionScan, scanning)
                }
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_scan -> {
                if (scanViewModel.valueScanning) {
                    bleScanManager.stopScan()
                } else {
                    bleScanManager.startScan(stopTimeout = 15000L)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun setScanTitle(actionScan: MenuItem, scanning: Boolean) {
        if (scanning) {
            actionScan.title = getString(R.string.action_stop_scan)
            actionScan.setIcon(R.drawable.ic_baseline_man_48)
        } else {
            actionScan.title = getString(R.string.action_start_scan)
            actionScan.setIcon(R.drawable.ic_baseline_directions_run_24)
        }
    }
}