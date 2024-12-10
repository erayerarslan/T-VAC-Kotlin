package com.erayerarslan.t_vac_kotlin.model

data class SensorData(
    val phValue: String = "0.0",
    val temperatureValue: String = "0.0",
    val conductibilityValue: String = "0",
    val fosforValue: String = "0",
    val humidityValue: String = "0",
    val potasyumValue: String = "0",
    val azotValue: String = "0"
)
object SensorDataManager {
    var sensorData: SensorData? = null
}




