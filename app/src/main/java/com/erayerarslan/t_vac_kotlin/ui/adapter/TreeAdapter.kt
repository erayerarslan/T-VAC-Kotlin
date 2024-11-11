package com.erayerarslan.t_vac_kotlin.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erayerarslan.t_vac_kotlin.databinding.ItemTreeBinding
import com.erayerarslan.t_vac_kotlin.model.Tree
import com.erayerarslan.t_vac_kotlin.model.treeList

class TreeAdapter(treeList: List<Tree>) : RecyclerView.Adapter<TreeAdapter.TreeViewHolder>() {

    inner class TreeViewHolder(val binding: ItemTreeBinding) : RecyclerView.ViewHolder(binding.root) {
        val treeName = binding.treeName
        val treeTemp = binding.treeTemp
        val treeHumadity = binding.treeHumadity
        val treeFeatures = binding.treeFeatures
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TreeAdapter.TreeViewHolder {
        val binding = ItemTreeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TreeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TreeAdapter.TreeViewHolder, position: Int) {
        val tree = treeList[position]
        holder.binding.treeImg.setImageResource(tree.img)
        holder.treeName.text = "Ağaç Türü: ${tree.name}"
        holder.treeTemp.text = "Sıcaklık: ${tree.temperatureRange.start} ile ${tree.temperatureRange.endInclusive} derece arasında"
        holder.treeHumadity.text = "Nem Oranı: ${tree.humidityRange.start} ile ${tree.humidityRange.endInclusive} % arasında"
        holder.treeFeatures.text = tree.features
    }

    override fun getItemCount(): Int {
        Log.d("TreeAdapter", "Item count: ${treeList.size}")
        return treeList.size // Liste boyutunu doğru şekilde döndürdüğünden emin olun
    }

    fun updateTreeList(newTreeList: List<Tree>) {
        treeList = newTreeList
        notifyDataSetChanged() // Liste güncellendiğinde görünümü yenileyin

    }
}