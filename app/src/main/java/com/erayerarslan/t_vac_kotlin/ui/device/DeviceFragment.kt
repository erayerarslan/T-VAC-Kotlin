package com.erayerarslan.t_vac_kotlin.ui.device

import android.bluetooth.BluetoothAdapter
import android.content.pm.PackageManager
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.erayerarslan.t_vac_kotlin.databinding.FragmentDeviceBinding
import com.erayerarslan.t_vac_kotlin.ui.adapter.DeviceAdapter
import dagger.hilt.android.AndroidEntryPoint
import android.Manifest
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.erayerarslan.t_vac_kotlin.model.Device


@AndroidEntryPoint
class DeviceFragment : Fragment() {
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var recyclerView: RecyclerView
    private lateinit var deviceAdapter: DeviceAdapter
    private var _binding: FragmentDeviceBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<DeviceViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions.all { it.value
            }) {
                // Tüm izinler verildi
                viewModel.startScanning()
            } else {
                Toast.makeText(requireContext(), "İzin verilmedi.", Toast.LENGTH_SHORT).show()
            }
        }
        checkPermissions()


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeviceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.deviceRecyclerView
        deviceAdapter = DeviceAdapter(emptyList()) { device ->
            pairDevice(device)
        }
        recyclerView.adapter = deviceAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.deviceList.observe(viewLifecycleOwner) { devices ->
            deviceAdapter.updateDeviceList(devices)
        }



    }








    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun checkPermissions() {
        val bluetoothScanPermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_SCAN)
        val bluetoothConnectPermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_CONNECT)
        val fineLocationPermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
        val coarseLocationPermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)

        // API 31 ve üzeri için
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (bluetoothScanPermission != PackageManager.PERMISSION_GRANTED ||
                bluetoothConnectPermission != PackageManager.PERMISSION_GRANTED ||
                fineLocationPermission != PackageManager.PERMISSION_GRANTED) {

                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_CONNECT,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                )
            } else {
                viewModel.startScanning()
            }
        } else {
            // API 30 ve altı için
            if (fineLocationPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            } else {
                viewModel.startScanning()
            }
        }
    }


    fun pairDevice(device: Device) {
        try {
            val bluetoothDevice = device.bluetoothDevice
            if (bluetoothDevice.bondState != BluetoothDevice.BOND_BONDED) {
                val success = bluetoothDevice.createBond()
                if (success) {
                    Toast.makeText(context, "Eşleşme isteği gönderildi.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Eşleşme isteği gönderilemedi.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Cihaz zaten eşleşmiş.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Eşleşme isteği gönderilemedi.", Toast.LENGTH_SHORT).show()
            println("Eşleşme isteği gönderilemedi: ${e.message}")
            println(context)
        }
    }



}
