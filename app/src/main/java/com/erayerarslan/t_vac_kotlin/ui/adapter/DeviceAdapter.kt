package com.erayerarslan.t_vac_kotlin.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.erayerarslan.t_vac_kotlin.R
import com.erayerarslan.t_vac_kotlin.databinding.ItemDeviceBinding
import com.erayerarslan.t_vac_kotlin.model.Device

class DeviceAdapter(private var deviceList: List<Device>,private val onDeviceClick: (Device) -> Unit)
    : RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder>() {
    inner class DeviceViewHolder(val binding: ItemDeviceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(device: Device) {
            binding.deviceName.text = device.name
            binding.deviceAddress.text = device.address
            itemView.setOnClickListener {
                onDeviceClick(device) // Cihaz tıklandığında onDeviceClick fonksiyonu çağrılır
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {

        val binding = ItemDeviceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeviceViewHolder(binding)
    }



    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        holder.bind(deviceList[position]) // Burada bir hata olabilir
    }

    override fun getItemCount(): Int = deviceList.size

    fun updateDeviceList(newDeviceList: List<Device>) {
        deviceList = newDeviceList
        notifyDataSetChanged() // Liste güncellendiğinde görünümü yenileyin
    }
}
