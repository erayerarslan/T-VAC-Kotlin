package com.erayerarslan.t_vac_kotlin.model

import com.erayerarslan.t_vac_kotlin.R

data class Tree(
    val name: String,
    val temperatureRange: IntRange,
    val humidityRange: IntRange,
    val features: String,
    val img:Int
){


    // Uygun sıcaklık ve nem aralıkları için kontrol işlevi
    fun isSuitable(temperature: Int, humidity: Int): Boolean {
        return temperature in temperatureRange && humidity in humidityRange
    }
    fun nameFilter(query: String): Boolean {
        return name.contains(query, ignoreCase = true)
    }
}

// Ağaç türleri listesi
var treeList = listOf(
    Tree("Meşe", 15..30, 40..70, "Güçlü ve dayanıklıdır, geniş alanlara ihtiyaç duyar.", R.drawable.ic_oak),
    Tree("Çam", -5..30, 30..60, "Soğuk iklimlere de dayanıklıdır, hızlı büyür.", R.drawable.ic_pine),
    Tree("Zeytin", 20..35, 30..50, "Sıcak ve kuru iklimleri sever, az su ister.", R.drawable.ic_olive),
    Tree("Kestane", 10..25, 60..80, "Nemli ortamları sever, verimli toprakta iyi gelişir.", R.drawable.ic_chestnut),
    Tree("Ceviz", 15..28, 50..70, "Derin ve nemli toprakları sever, geniş kök yapısına ihtiyaç duyar.", R.drawable.ic_walnut),
    Tree("Akasya", 15..35, 20..40, "Kuraklığa dayanıklıdır, az su gerektirir.", R.drawable.ic_acacia),
    Tree("Kavak", 10..25, 50..70, "Su kaynaklarına yakın bölgelerde daha hızlı büyür.", R.drawable.ic_poplar),
    Tree("Ardıç", 5..25, 30..50, "Soğuk ve kurak iklimlerde dayanıklıdır, dağlık alanlarda iyi yetişir.", R.drawable.ic_juniper),
    Tree("Söğüt", 15..30, 60..80, "Su kenarlarında ve nemli topraklarda yetişir, hızla büyür.", R.drawable.ic_willow),
    Tree("Eray", 0..50, 0..50, "Soğuk ve kurak iklimlerde dayanıklıdır, dağlık alanlarda iyi yetişir.", R.drawable.ic_juniper),
    Tree("Erarslan", 0..50, 0..50, "Soğuk ve kurak iklimlerde dayanıklıdır, dağlık alanlarda iyi yetişir.", R.drawable.ic_juniper),
)
