package com.erayerarslan.t_vac_kotlin.model

data class Tree(
    val name: String,
    val temperatureRange: IntRange,
    val humidityRange: IntRange,
    val features: String
){
    // Uygun sıcaklık ve nem aralıkları için kontrol işlevi
    fun isSuitable(temperature: Int, humidity: Int): Boolean {
        return temperature in temperatureRange && humidity in humidityRange
    }
}

// Ağaç türleri listesi
val treeList = listOf(
    Tree("Meşe", 15..30, 40..70, "Güçlü ve dayanıklıdır, geniş alanlara ihtiyaç duyar."),
    Tree("Çam", -5..30, 30..60, "Soğuk iklimlere de dayanıklıdır, hızlı büyür."),
    Tree("Zeytin", 20..35, 30..50, "Sıcak ve kuru iklimleri sever, az su ister."),
    Tree("Kestane", 10..25, 60..80, "Nemli ortamları sever, verimli toprakta iyi gelişir."),
    Tree("Ceviz", 15..28, 50..70, "Derin ve nemli toprakları sever, geniş kök yapısına ihtiyaç duyar."),
    Tree("Akasya", 15..35, 20..40, "Kuraklığa dayanıklıdır, az su gerektirir."),
    Tree("Kavak", 10..25, 50..70, "Su kaynaklarına yakın bölgelerde daha hızlı büyür."),
    Tree("Ardıç", 5..25, 30..50, "Soğuk ve kurak iklimlerde dayanıklıdır, dağlık alanlarda iyi yetişir."),
    Tree("Söğüt", 15..30, 60..80, "Su kenarlarında ve nemli topraklarda yetişir, hızla büyür.")
)
