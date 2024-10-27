package com.erayerarslan.t_vac_kotlin.repository

import androidx.lifecycle.LiveData
import com.erayerarslan.t_vac_kotlin.model.Device

interface DeviceRepository  {
    suspend fun getDevices(): LiveData<List<Device>>
}