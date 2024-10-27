package com.erayerarslan.t_vac_kotlin.ui.device

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erayerarslan.t_vac_kotlin.model.Device // Device sınıfı için
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DeviceViewModel @Inject constructor(
    private val bluetoothAdapter: BluetoothAdapter
) : ViewModel() {

    private val _deviceList = MutableLiveData<List<Device>>()
    val deviceList: LiveData<List<Device>> = _deviceList


    fun startScanning() {
        val discoveredDevices = mutableListOf<Device>()
        val bluetoothLeScanner: BluetoothLeScanner? = bluetoothAdapter.bluetoothLeScanner


        val leScanCallback = object : ScanCallback() {

            override fun onScanResult(callbackType: Int, result: android.bluetooth.le.ScanResult?) {
                val device = result?.device
                device?.let {
                    val deviceName = it.name ?: "Bilinmeyen Cihaz"
                    val newDevice = Device(deviceName, it.address,it)

                    // Cihazın taranıp taranmadığını loglayın
                    Log.d("BluetoothScan", "Cihaz adı: $deviceName, Adres: ${it.address}")

                    if (!discoveredDevices.contains(newDevice)) {
                        discoveredDevices.add(newDevice)
                        _deviceList.postValue(discoveredDevices)
                    }
                }
            }

            override fun onScanFailed(errorCode: Int) {
                println("Taraması başarısız oldu. Hata kodu: $errorCode")
            }
        }

        bluetoothLeScanner?.startScan(leScanCallback)

        // 5 saniye tarama yap ve sonra durdur
        viewModelScope.launch {

            delay(5000)
            bluetoothLeScanner?.stopScan(leScanCallback)
        }
    }
   fun getSelectedDevice(): Device? {
        return _deviceList.value?.firstOrNull()
   }

}

