package com.erayerarslan.t_vac_kotlin.ui.home

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.erayerarslan.t_vac_kotlin.R
import com.erayerarslan.t_vac_kotlin.databinding.FragmentHomeBinding
import com.erayerarslan.t_vac_kotlin.model.Tree
import com.erayerarslan.t_vac_kotlin.model.treeList
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fab : FloatingActionButton =binding.fab
        fab.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_deviceFragment)

        }
        observeEvents()
        viewModel.fetchTreeList(30,70)
    }
    private fun observeEvents() {
        viewModel.filteredTreeList.observe(viewLifecycleOwner) { treeList ->
            binding.treeName.text = treeList.firstOrNull()?.name ?: ""
            binding.treeTemp.text = treeList.firstOrNull()?.temperatureRange?.toString() ?: ""
            val range=binding.treeTemp.text.toString().replace(".."," ile ") +" derece arasında"
            binding.treeTemp.text=range
            binding.treeHumadity.text = treeList.firstOrNull()?.humidityRange?.toString() ?: ""
            binding.treeFeatures.text = treeList.firstOrNull()?.features ?: ""
        }
    }
}