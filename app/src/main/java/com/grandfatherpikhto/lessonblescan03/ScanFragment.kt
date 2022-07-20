package com.grandfatherpikhto.lessonblescan03

import android.bluetooth.BluetoothDevice
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lessonblescan03.R
import com.grandfatherpikhto.lessonblescan03.adapters.RvBleDevicesAdapter
import com.grandfatherpikhto.lessonblescan03.adapters.RvClickItemListener
import com.example.lessonblescan03.databinding.FragmentScanBinding
import com.grandfatherpikhto.lessonblescan03.models.MainActivityViewModel
import com.grandfatherpikhto.lessonblescan03.models.ScanViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ScanFragment : Fragment() {

    private var _binding: FragmentScanBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val rvBleDevicesAdapter = RvBleDevicesAdapter()

    private val scanViewModel by viewModels<ScanViewModel>()
    private val mainActivityViewModel by activityViewModels<MainActivityViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentScanBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            rvBleDevices.adapter = rvBleDevicesAdapter
            rvBleDevices.layoutManager = LinearLayoutManager(requireContext())
        }
        rvBleDevicesAdapter.addItems(scanViewModel.bleDevices)
        lifecycleScope.launch {
            scanViewModel.stateFlowDevice.filterNotNull().collect { device ->
                rvBleDevicesAdapter.addItem(device)
            }
        }
/*
        // Using Interface
        rvBleDevicesAdapter.setClickItemListener(object : RvClickItemListener<BluetoothDevice> {
            override fun onClickItem(model: BluetoothDevice, view: View) {
                mainActivityViewModel.changeCurrentDevice(model)
                this@ScanFragment
                    .activity
                    ?.findNavController(R.id.nav_host_fragment_content_main)
                    ?.navigate(R.id.action_FirstFragment_to_SecondFragment)
                super.onClickItem(model, view)
            }
        })
*/
        /**
         * Using High-Order functions
         * https://kotlinlang.org/docs/lambdas.html
         */
        rvBleDevicesAdapter.setOnClickListener { bluetoothDevice, _ ->
            mainActivityViewModel.changeCurrentDevice(bluetoothDevice)
            this@ScanFragment
                .activity
                ?.findNavController(R.id.nav_host_fragment_content_main)
                ?.navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}