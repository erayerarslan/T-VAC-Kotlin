package com.erayerarslan.t_vac_kotlin.ui.home

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erayerarslan.t_vac_kotlin.R
import com.erayerarslan.t_vac_kotlin.databinding.FragmentHomeBinding
import com.erayerarslan.t_vac_kotlin.model.Tree
import com.erayerarslan.t_vac_kotlin.ui.adapter.TreeAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TreeAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        recyclerView = binding.treeRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fab : FloatingActionButton =binding.fab
        fab.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_deviceFragment)

        }
        adapter = TreeAdapter(emptyList())
        recyclerView.adapter = adapter
//        observeEvents()
        viewModel.fetchTreeList(25,60)
        viewModel.filteredTreeList.observe(viewLifecycleOwner) { tree ->
            if (tree.isNotEmpty()) {
                adapter.updateTreeList(tree)
            } else {
                // Geriye boş bir liste geliyorsa, belki filtreleme kriterleriniz çok katı
                adapter.updateTreeList(emptyList())
            }

        }
    }
//    private fun observeEvents() {
//        viewModel.filteredTreeList.observe(viewLifecycleOwner) { treeList ->
//            binding.treeName.text = treeList.firstOrNull()?.name ?: ""
//            binding.treeTemp.text = treeList.firstOrNull()?.temperatureRange?.toString() ?: ""
//            val range=binding.treeTemp.text.toString().replace(".."," ile ") +" derece arasında"
//            binding.treeTemp.text=range
//            binding.treeHumadity.text = treeList.firstOrNull()?.humidityRange?.toString() ?: ""
//            binding.treeFeatures.text = treeList.firstOrNull()?.features ?: ""
//        }
//    }
}