package com.erayerarslan.t_vac_kotlin.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.erayerarslan.t_vac_kotlin.databinding.ItemTreeBinding
import com.erayerarslan.t_vac_kotlin.model.Tree
import com.erayerarslan.t_vac_kotlin.model.treeList
import com.erayerarslan.t_vac_kotlin.util.loadImage

class SearchAdapter (treeList: List<Tree>) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {
    private var treeList2: List<Tree> = treeList
    inner class SearchViewHolder(val binding: ItemTreeBinding) : RecyclerView.ViewHolder(binding.root) {
        val treeName = binding.treeName
        val treeTemp = binding.treeTemp
        val treeHumadity = binding.treeHumadity
        val treeFeatures = binding.treeFeatures
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.SearchViewHolder {
        val binding = ItemTreeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchAdapter.SearchViewHolder, position: Int) {
        val tree = treeList2[position]
        holder.binding.treeImg.loadImage(tree.img)
        holder.treeName.text = "Ağaç Türü: ${tree.name}"
        holder.treeTemp.text = "Sıcaklık: ${tree.temperatureRange.start} ile ${tree.temperatureRange.endInclusive} derece arasında"
        holder.treeHumadity.text = "Nem Oranı: ${tree.humidityRange.start} ile ${tree.humidityRange.endInclusive} % arasında"
        holder.treeFeatures.text = tree.features
    }

    override fun getItemCount(): Int {
        Log.d("TreeAdapter", "Item count: ${treeList2.size}")
        return treeList2.size // Liste boyutunu doğru şekilde döndürdüğünden emin olun
    }

    fun updateTreeList(newTreeList: List<Tree>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize() = treeList2.size
            override fun getNewListSize() = newTreeList.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                treeList2[oldItemPosition].name == newTreeList[newItemPosition].name

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                treeList2[oldItemPosition] == newTreeList[newItemPosition]
        }

        val diffResult = DiffUtil.calculateDiff(diffCallback)
        treeList2 = newTreeList
        diffResult.dispatchUpdatesTo(this)
    }

}