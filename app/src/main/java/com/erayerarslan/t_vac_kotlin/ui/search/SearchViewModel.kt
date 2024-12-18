package com.erayerarslan.t_vac_kotlin.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erayerarslan.t_vac_kotlin.model.Tree
import com.erayerarslan.t_vac_kotlin.model.treeList


class SearchViewModel : ViewModel() {
    private val _filteredTreeListSearch = MutableLiveData<List<Tree>>()
    val filteredTreeListSearch: LiveData<List<Tree>> get() = _filteredTreeListSearch

   val searchFilterList = treeList.toList()

    fun fetchTreeList(name: String) {
        val filteredList = searchFilterList.filter { tree ->
            tree.nameFilter(name)

        }
        Log.d("HomeViewModel", "Filtered tree list: $filteredList") // Filtrelenen listeyi logla
        _filteredTreeListSearch.value = filteredList
    }


}
