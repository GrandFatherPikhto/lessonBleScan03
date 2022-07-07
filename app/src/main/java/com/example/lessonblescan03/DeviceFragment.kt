package com.example.lessonblescan03

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.lessonblescan03.databinding.FragmentDeviceBinding
import com.example.lessonblescan03.models.MainActivityViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class DeviceFragment : Fragment() {
    companion object {

    }

    private val ARG_DEVICE_NAME="arg_device_name"
    private val ARG_DEVICE_ADDRESS="arg_device_address"

    private var _binding: FragmentDeviceBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val mainActivityViewModel by activityViewModels<MainActivityViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDeviceBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnBack.setOnClickListener {
                findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
            }
            tvDeviceName.text = mainActivityViewModel.currentDevice?.name ?:
                getString(R.string.ble_default_device_name)
            tvDeviceAddress.text = mainActivityViewModel.currentDevice?.address ?:
                getString(R.string.ble_default_device_address)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}