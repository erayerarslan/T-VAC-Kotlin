package com.erayerarslan.t_vac_kotlin.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erayerarslan.t_vac_kotlin.model.Tree
import com.erayerarslan.t_vac_kotlin.model.treeList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel (
): ViewModel() {
    private val _filteredTreeList = MutableLiveData<List<Tree>>()
    val filteredTreeList: LiveData<List<Tree>> get() = _filteredTreeList



    fun fetchTreeList(temperature: Int, humidity: Int) {
        val filteredList = treeList.filter { tree ->
            tree.isSuitable(temperature, humidity)

        }
        Log.d("HomeViewModel", "Filtered tree list: $filteredList") // Filtrelenen listeyi logla
        _filteredTreeList.value = filteredList
    }


}