package com.erayerarslan.t_vac_kotlin.repository

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import android.content.Context
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.erayerarslan.t_vac_kotlin.model.Device
import javax.inject.Inject
import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast

