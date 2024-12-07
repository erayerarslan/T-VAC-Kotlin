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
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.erayerarslan.t_vac_kotlin.model.Device
import kotlinx.coroutines.delay as delay1

@AndroidEntryPoint
class DeviceFragment : Fragment() {
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var recyclerView: RecyclerView
    private lateinit var deviceAdapter: DeviceAdapter
    private var _binding: FragmentDeviceBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<DeviceViewModel>()
    private var selectedDevice: Device? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions.all { it.value }) {
                viewModel.startDiscovery()
            } else {
                Toast.makeText(requireContext(), "Gerekli izinler verilmedi.", Toast.LENGTH_SHORT).show()
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
            selectedDevice = device
            Toast.makeText(requireContext(), "Seçilen cihaz: ${device.name}", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = deviceAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.devices.observe(viewLifecycleOwner) { devices ->
            deviceAdapter.updateDeviceList(devices)
        }
        binding.btnStartDiscovery.setOnClickListener {
            val selectedDevice = getSelectedDevice() // Seçilen cihazı al
            if (selectedDevice != null) {
                // Cihaz eşleşmesini başlat
                viewModel.pairDevice(selectedDevice,
                    onSuccess = {
                        // Başarılı eşleşme sonrası veri alma işlemini başlat
                        Toast.makeText(requireContext(), "Tarama Başlatılıyor: ${selectedDevice.name}", Toast.LENGTH_SHORT).show()
                        binding.progressBar.visibility = View.VISIBLE

                        viewModel.listenForData(selectedDevice.bluetoothDevice) // Veri alma işlemi
                        binding.progressBar.visibility = View.GONE
                        findNavController().popBackStack()

                    },
                    onError = { error ->
                        // Eşleşme başarısızsa kullanıcıya bilgi göster
                        Toast.makeText(requireContext(), "Eşleşme başarısız: $error", Toast.LENGTH_SHORT).show()
                    }
                )
            } else {
                // Cihaz seçilmediyse uyarı ver
                Toast.makeText(requireContext(), "Lütfen bir cihaz seçin!", Toast.LENGTH_SHORT).show()
            }
        }

    }
    fun getSelectedDevice(): Device? {
        return selectedDevice
    }
    private fun checkPermissions() {
        val permissionsNeeded = mutableListOf<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(Manifest.permission.BLUETOOTH_CONNECT)
            }
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(Manifest.permission.BLUETOOTH_SCAN)
            }
        } else {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }

        if (permissionsNeeded.isNotEmpty()) {
            requestPermissionLauncher.launch(permissionsNeeded.toTypedArray())
        } else {
            viewModel.startDiscovery()
        }
    }

    private fun pairDevice(device: Device) {
        viewModel.pairDevice(device,
            onSuccess = {
                Toast.makeText(requireContext(), "Eşleşme başarılı: ${device.name}", Toast.LENGTH_SHORT).show()


            },
            onError = { error ->
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        )
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
