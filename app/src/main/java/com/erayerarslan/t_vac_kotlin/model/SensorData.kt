package com.erayerarslan.t_vac_kotlin.model

data class SensorData(
    val phValue: String = "1",
    val temperatureValue: String = "1",
    val conductibilityValue: String = "1",
    val fosforValue: String = "1",
    val humidityValue: String = "1",
    val potasyumValue: String = "1",
    val azotValue: String = "1"
)
object SensorDataManager {
    var sensorData: SensorData? = null
}




