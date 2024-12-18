package com.erayerarslan.t_vac_kotlin.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erayerarslan.t_vac_kotlin.model.Tree
import com.erayerarslan.t_vac_kotlin.model.treeList

class HomeViewModel (
): ViewModel() {
    private val _filteredTreeList = MutableLiveData<List<Tree>>()
    val filteredTreeList: LiveData<List<Tree>> get() = _filteredTreeList

    val homefilterlist = treeList

    fun fetchTreeList(temperature: Int, humidity: Int) {
        val filteredList = homefilterlist.filter { tree ->
            tree.isSuitable(temperature, humidity)

        }
        Log.d("HomeViewModel", "Filtered tree list: $filteredList") // Filtrelenen listeyi logla
        _filteredTreeList.value = filteredList
    }


}