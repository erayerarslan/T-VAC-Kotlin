package com.erayerarslan.t_vac_kotlin.model

import android.bluetooth.BluetoothDevice

data class Device(
    val name: String,
    val address: String,
    val bluetoothDevice: BluetoothDevice,
    val isPaired: Boolean = false
)
