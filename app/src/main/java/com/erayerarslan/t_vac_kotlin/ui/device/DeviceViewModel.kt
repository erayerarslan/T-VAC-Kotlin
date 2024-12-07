package com.erayerarslan.t_vac_kotlin.ui.device
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.erayerarslan.t_vac_kotlin.model.Device
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class DeviceViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val _devices = MutableLiveData<List<Device>>()
    val devices: LiveData<List<Device>> get() = _devices

    private val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private val discoveredDevices = mutableListOf<Device>()
    private var bondReceiver: BroadcastReceiver? = null
    private val appContext = application.applicationContext
    private var phValue2: String? = null
    private var temperatureValue2: String? = null
    private var conductibilityValue2: String? = null
    private var fosforValue2: String? = null
    private var humidityValue2: String? = null
    private var potasyumValue2: String? = null
    private var azotValue2: String? = null


    fun startDiscovery() {
        if (!bluetoothAdapter.isEnabled) {
            Toast.makeText(appContext, "Bluetooth kapalı, lütfen açın!", Toast.LENGTH_SHORT).show()
            return
        }

        if (bluetoothAdapter.isDiscovering) {
            bluetoothAdapter.cancelDiscovery()
        }

        val discoveryReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val action = intent.action
                if (BluetoothDevice.ACTION_FOUND == action) {
                    val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                    device?.let {
                        val deviceName = it.name ?: "Bilinmeyen Cihaz"
                        val newDevice = Device(deviceName, it.address, it)

                        if (!discoveredDevices.any { d -> d.address == newDevice.address }) {
                            discoveredDevices.add(newDevice)
                            _devices.postValue(discoveredDevices)
                            Log.d("BluetoothDiscovery", "Cihaz bulundu: $deviceName - ${it.address}")
                        }
                    }
                }
            }
        }

        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        appContext.registerReceiver(discoveryReceiver, filter)

        bluetoothAdapter.startDiscovery()
    }

    fun pairDevice(device: Device, onSuccess: () -> Unit, onError: (String) -> Unit) {
        try {
            val bluetoothDevice = device.bluetoothDevice
            if (bluetoothDevice.bondState != BluetoothDevice.BOND_BONDED) {
                bondReceiver = object : BroadcastReceiver() {
                    override fun onReceive(context: Context, intent: Intent) {
                        val action = intent.action
                        if (BluetoothDevice.ACTION_BOND_STATE_CHANGED == action) {
                            val bondedDevice = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                            val bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1)

                            if (bondedDevice?.address == bluetoothDevice.address) {
                                when (bondState) {
                                    BluetoothDevice.BOND_BONDED -> {
                                        onSuccess()
                                        unregisterReceiver()

                                    }
                                    BluetoothDevice.BOND_NONE -> {
                                        onError("Eşleşme başarısız.")
                                        unregisterReceiver()
                                    }
                                }
                            }
                        }
                    }
                }

                val filter = IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
                appContext.registerReceiver(bondReceiver, filter)

                bluetoothDevice.createBond()
            } else {
                onSuccess()
            }
        } catch (e: Exception) {
            Log.e("PairDevice", "Hata: ${e.message}")
            onError("Eşleşme hatası: ${e.message}")
        }
    }
    private var buffer = StringBuilder()

    fun listenForData(device: BluetoothDevice) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
                val socket = device.createRfcommSocketToServiceRecord(uuid)
                socket.connect()

                val inputStream = socket.inputStream
                val bufferArray = ByteArray(1024)
                var bytes: Int

                while (true) {
                    bytes = inputStream.read(bufferArray)
                    val receivedData = String(bufferArray, 0, bytes)
                    phValue2= null
                    temperatureValue2= null
                    conductibilityValue2= null
                    fosforValue2= null
                    humidityValue2= null
                    potasyumValue2= null

                    // Gelen veriyi tamponda birleştir
                    buffer.append(receivedData)

                    // Mesajın tamamlandığını kontrol et
                    if (buffer.contains("\n")) {
                        val completeMessage = buffer.toString()
                        buffer.clear() // Tamponu temizle
                         Log.d("BluetoothData", "Tam mesaj: $completeMessage")

                        val phValue = extractPhValue(completeMessage)
                        if (phValue != null && phValue != "0.00") {
                              phValue2 = phValue
                            println("phValue2: " + phValue2)
                        }
                        val temperatureValue = extractTemperatureValue(completeMessage)
                        if (temperatureValue != null && temperatureValue != "0.00") {
                             temperatureValue2 = temperatureValue
                            println("temperatureValue2: " + temperatureValue2)
                        }
                        val conductibilityValue = extractConductibilityValue(completeMessage)
                        if (conductibilityValue != null && conductibilityValue != "0") {
                             conductibilityValue2 = conductibilityValue
                            println("conductibilityValue2: "+conductibilityValue2)
                        }
                        val fosforValue = extractFosforValue(completeMessage)
                        if (fosforValue != null && fosforValue != "0") {
                            fosforValue2 = fosforValue
                            println("fosforValue2: "+fosforValue2)
                        }
                        val humidityValue = extractHumidityValue(completeMessage)
                        if ( humidityValue != null && humidityValue != "0" && humidityValue != "0.00") {
                            humidityValue2 = humidityValue
                            println("humidityValue2: "+humidityValue2)
                        }
                        val potasyumValue = extractPotasyumValue(completeMessage)
                        if (potasyumValue != null && potasyumValue != "0" && potasyumValue != "0.00") {
                            potasyumValue2 = potasyumValue

                            println("potasyumValue2: "+potasyumValue2)
                        }
                        val azotValue = extractAzotValue(completeMessage)
                        if (azotValue != null && azotValue != "0" && azotValue != "0.00") {
                            azotValue2 = azotValue

                            println("azotValue2: "+azotValue2)
                        }
                        if (!phValue2.isNullOrEmpty() && !temperatureValue2.isNullOrEmpty() && !conductibilityValue2.isNullOrEmpty() && !fosforValue2.isNullOrEmpty() && !humidityValue2.isNullOrEmpty() && !potasyumValue2.isNullOrEmpty() && !azotValue2.isNullOrEmpty()){
                            println("başarılı")
                            break
                        }
                        delay(5000)

                    }

                }
            } catch (e: Exception) {
                Log.e("BluetoothData", "Veri alma hatası: ${e.message}")
            }
        }

    }

    private fun extractPhValue(message: String): String? {
        val regex = """pH:\s*([\d.]+)""" // pH: ve ardından sayıyı yakalar
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(message)

        return if (matcher.find()) {
            matcher.group(1) // Parantez içindeki yakalanan değeri döndür
        } else {
            null // Eğer pH bulunamazsa null döner
        }
    }
    private fun extractTemperatureValue(message: String): String? {
        val regex = """Sicaklik:\s*([\d.]+)""" // pH: ve ardından sayıyı yakalar
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(message)

        return if (matcher.find()) {
            matcher.group(1) // Parantez içindeki yakalanan değeri döndür
        } else {
            null // Eğer pH bulunamazsa null döner
        }
    }
    private fun extractConductibilityValue(message: String): String? {
        val regex = """Iletkenlik:\s*([\d.]+)""" // pH: ve ardından sayıyı yakalar
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(message)

        return if (matcher.find()) {
            matcher.group(1) // Parantez içindeki yakalanan değeri döndür
        } else {
            null // Eğer pH bulunamazsa null döner
        }
    }
    private fun extractFosforValue(message: String): String? {
        val regex = """Fosfor:\s*([\d.]+)""" // pH: ve ardından sayıyı yakalar
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(message)

        return if (matcher.find()) {
            matcher.group(1) // Parantez içindeki yakalanan değeri döndür
        } else {
            null // Eğer pH bulunamazsa null döner
        }
    }
    private fun extractHumidityValue(message: String): String? {
        val regex = """Nem:\s*([\d.]+)""" // pH: ve ardından sayıyı yakalar
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(message)

        return if (matcher.find()) {
            matcher.group(1) // Parantez içindeki yakalanan değeri döndür
        } else {
            null // Eğer pH bulunamazsa null döner
        }
    }
    private fun extractAzotValue(message: String): String? {
        val regex = """Nem:\s*([\d.]+)""" // pH: ve ardından sayıyı yakalar
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(message)

        return if (matcher.find()) {
            matcher.group(1) // Parantez içindeki yakalanan değeri döndür
        } else {
            null // Eğer pH bulunamazsa null döner
        }
    }
    private fun extractPotasyumValue(message: String): String? {
        val regex = """Potasyum:\s*([\d.]+)""" // pH: ve ardından sayıyı yakalar
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(message)

        return if (matcher.find()) {
            matcher.group(1) // Parantez içindeki yakalanan değeri döndür
        } else {
            null // Eğer pH bulunamazsa null döner
        }
    }
    private fun unregisterReceiver() {
        bondReceiver?.let {
            appContext.unregisterReceiver(it)
            bondReceiver = null
        }
    }
}
