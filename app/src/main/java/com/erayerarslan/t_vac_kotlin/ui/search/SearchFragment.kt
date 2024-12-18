package com.erayerarslan.t_vac_kotlin.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erayerarslan.t_vac_kotlin.R
import com.erayerarslan.t_vac_kotlin.databinding.FragmentHomeBinding
import com.erayerarslan.t_vac_kotlin.databinding.FragmentSearchBinding
import com.erayerarslan.t_vac_kotlin.model.treeList
import com.erayerarslan.t_vac_kotlin.ui.adapter.SearchAdapter
import com.erayerarslan.t_vac_kotlin.ui.adapter.TreeAdapter
import com.erayerarslan.t_vac_kotlin.ui.device.DeviceViewModel
import com.erayerarslan.t_vac_kotlin.ui.home.HomeViewModel

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<SearchViewModel>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        recyclerView = binding.treeRecyclerViewSearch
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SearchAdapter(viewModel.searchFilterList)
        recyclerView.adapter = adapter
        observeEvents()
        binding.searchButton.setOnClickListener {
            val name = binding.searchEditText.text.toString()
            viewModel.fetchTreeList(name)
        }

    }



    private fun observeEvents() {

        viewModel.filteredTreeListSearch.observe(viewLifecycleOwner) { tree ->
            if (tree.isNotEmpty()) {
                adapter.updateTreeList(tree)
            } else {
                adapter.updateTreeList(emptyList())
            }

        }
    }
}