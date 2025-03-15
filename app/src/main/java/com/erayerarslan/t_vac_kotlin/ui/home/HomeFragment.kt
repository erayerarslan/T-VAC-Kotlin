package com.erayerarslan.t_vac_kotlin.ui.home

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.erayerarslan.t_vac_kotlin.R
import com.erayerarslan.t_vac_kotlin.databinding.FragmentHomeBinding
import com.erayerarslan.t_vac_kotlin.model.SensorDataManager
import com.erayerarslan.t_vac_kotlin.ui.adapter.TreeAdapter
import com.erayerarslan.t_vac_kotlin.ui.device.DeviceViewModel
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import com.github.mikephil.charting.components.XAxis


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeViewModel>()
    private val deviceViewModel: DeviceViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TreeAdapter

    private lateinit var soilMoistureChart: HorizontalBarChart
    private lateinit var phValueChart: HorizontalBarChart
    private lateinit var conductivityChart: HorizontalBarChart
    private lateinit var phosphorusChart: HorizontalBarChart
    private lateinit var potassiumChart: HorizontalBarChart
    private lateinit var nitrogenChart: HorizontalBarChart
    private lateinit var temperatureChart: HorizontalBarChart


    private lateinit var phValue:String
    private lateinit var temperatureValue :String
    private lateinit var conductibilityValue :String
    private lateinit var fosforValue :String
    private lateinit var humidityValue:String
    private lateinit var potasyumValue:String
    private lateinit var azotValue:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        recyclerView = binding.treeRecyclerViewHome
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val swipeRefreshLayout: SwipeRefreshLayout = binding.swipeRefreshLayout

        val fab: FloatingActionButton = binding.fab
        fab.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_deviceFragment)

        }


        phValue = SensorDataManager.sensorData?.phValue ?: "1"
        temperatureValue =SensorDataManager.sensorData?.temperatureValue ?: "1"
        conductibilityValue = SensorDataManager.sensorData?.conductibilityValue?: "1"
        fosforValue = SensorDataManager.sensorData?.fosforValue?: "1"
        humidityValue = SensorDataManager.sensorData?.humidityValue?: "1"
        potasyumValue = SensorDataManager.sensorData?.potasyumValue?: "1"
        azotValue = SensorDataManager.sensorData?.azotValue?: "1"

        adapter = TreeAdapter(emptyList())
        recyclerView.isVisible = false
        recyclerView.adapter = adapter
        observeEvents()

        swipeRefreshLayout.setOnRefreshListener {

            refreshData()


            swipeRefreshLayout.isRefreshing = false // Yükleme tamamlandığında animasyonu durdur
        }
        setupUI()
        setupCharts()
        loadData()
    }

    override fun onResume() {
        super.onResume()
        adapter = TreeAdapter(emptyList())
        recyclerView.adapter = adapter
        refreshData()
    }

    private fun refreshData() {
        // Örnek: ViewModel'deki metotlarla veriyi tekrar sorgulama
        val temperature = SensorDataManager.sensorData?.temperatureValue?.toFloat()?.toInt()
        val humidity = SensorDataManager.sensorData?.humidityValue?.toFloat()?.toInt()

        if (temperature != null && humidity != null) {
            viewModel.fetchTreeList(temperature, humidity)
        }

    }

    private fun observeEvents() {

        viewModel.filteredTreeList.observe(viewLifecycleOwner) { tree ->
            if (tree.isNotEmpty()) {
                adapter.updateTreeList(tree)
            } else {

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

    private fun setupUI() {
        // Set gradient background for header
        val headerCard = binding.headerCard
        val gradientDrawable = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(
                Color.parseColor("#5B68DF"),
                Color.parseColor("#FFA555")
            )
        )

        binding.headerLayout.background = gradientDrawable

        // Initialize chart views
        soilMoistureChart = binding.soilMoistureChart
        phValueChart = binding.phValueChart
        conductivityChart = binding.conductivityChart
        phosphorusChart = binding.phosphorusChart
        potassiumChart = binding.potassiumChart
        nitrogenChart = binding.nitrogenChart
        temperatureChart = binding.temperatureChart

        // Set up buttons
        val exportButton = binding.exportButton
        val analysisButton = binding.analysisButton

        exportButton.setOnClickListener {

        }

        analysisButton.setOnClickListener {
            // Handle analysis functionality
        }
    }

    private fun setupCharts() {
        val charts = listOf(
            soilMoistureChart,
            phValueChart,
            conductivityChart,
            phosphorusChart,
            potassiumChart,
            nitrogenChart,
            temperatureChart
        )

        charts.forEach { chart ->
            // Disable grid lines, legend, and description
            chart.setDrawGridBackground(false)
            chart.description.isEnabled = false
            chart.legend.isEnabled = false
            chart.setTouchEnabled(false)
            chart.setScaleEnabled(false)
            chart.setPinchZoom(false)

            // Configure axes
            chart.axisLeft.isEnabled = false
            chart.axisRight.isEnabled = false

            val xAxis = chart.xAxis
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            xAxis.setDrawAxisLine(false)
            xAxis.setDrawLabels(false)

            // Set fixed chart dimensions
            chart.minimumHeight = resources.getDimensionPixelSize(R.dimen.chart_height)
            chart.setViewPortOffsets(0f, 0f, 0f, 0f)
        }
    }

    private fun loadData() {
        humidityValue.toFloat().let {
            setupParameterChart(soilMoistureChart, "Soil Moisture",
                it, getHumidityColor(it), getHumidityStatus(it))
        }
        phValue.toFloat().let {
            setupParameterChart(phValueChart, "pH Value",
                it, getPhColor(it), getPhStatus(it))
        }
        conductibilityValue.toFloat().let {
            setupParameterChart(conductivityChart, "Conductivity",
                it, getConductivityColor(it), getConductivityStatus(it))
        }
        fosforValue.toFloat()?.let {
            setupParameterChart(phosphorusChart, "Phosphorus",
                it, getPhosphorusColor(it), getPhosphorusStatus(it))
        }
        potasyumValue.toFloat().let {
            setupParameterChart(potassiumChart, "Potassium",
                it, getPotassiumColor(it), getPotassiumStatus(it))
        }
        azotValue.toFloat().let {
            setupParameterChart(nitrogenChart, "Nitrogen",
                it, getNitrogenColor(it), getNitrogenStatus(it))
        }
        temperatureValue.toFloat().let {
            setupParameterChart(temperatureChart, "Temperature",
                it, getTemperatureColor(it), getTemperatureStatus(it))
            println(temperatureValue)
        }
    }

    private fun getPhStatus(value: Float): String {
        return when {
            value < 5.0 -> "Very Low"
            value < 5.5 -> "Low"
            value < 6.5 -> "Normal"
            value < 7.5 -> "High"
            else -> "Very High"
        }
    }

    private fun getPhColor(value: Float): String {
        return when {
            value < 5.0 -> "#FF0000" // Kırmızı
            value < 5.5 -> "#FFA500" // Turuncu
            value < 6.5 -> "#00FF00" // Yeşil
            value < 7.5 -> "#FFFF00" // Sarı
            else -> "#8A2BE2" // Mor
        }
    }

    private fun getTemperatureStatus(value: Float): String {
        return when {
            value < 5 -> "Very Low"
            value < 10 -> "Low"
            value < 25 -> "Normal"
            value < 30 -> "High"
            else -> "Very High"
        }
    }

    private fun getTemperatureColor(value: Float): String {
        return when {
            value < 5 -> "#0000FF" // Mavi
            value < 10 -> "#1E90FF" // Açık Mavi
            value < 25 -> "#00FF00" // Yeşil
            value < 30 -> "#FFFF00" // Sarı
            else -> "#FF4500" // Kırmızı-Turuncu
        }
    }

    private fun getConductivityStatus(value: Float): String {
        return when {
            value < 0.2 -> "Very Low"
            value < 0.5 -> "Low"
            value < 1.5 -> "Normal"
            value < 2.5 -> "High"
            else -> "Very High"
        }
    }

    private fun getConductivityColor(value: Float): String {
        return when {
            value < 0.2 -> "#FF0000" // Kırmızı
            value < 0.5 -> "#FFA500" // Turuncu
            value < 1.5 -> "#00FF00" // Yeşil
            value < 2.5 -> "#FFFF00" // Sarı
            else -> "#8A2BE2" // Mor
        }
    }

    private fun getPhosphorusStatus(value: Float): String {
        return when {
            value < 5 -> "Very Low"
            value < 10 -> "Low"
            value < 30 -> "Normal"
            value < 50 -> "High"
            else -> "Very High"
        }
    }

    private fun getPhosphorusColor(value: Float): String {
        return when {
            value < 5 -> "#FF0000"
            value < 10 -> "#FFA500"
            value < 30 -> "#00FF00"
            value < 50 -> "#FFFF00"
            else -> "#8A2BE2"
        }
    }

    private fun getPotassiumStatus(value: Float): String {
        return when {
            value < 50 -> "Very Low"
            value < 100 -> "Low"
            value < 200 -> "Normal"
            value < 300 -> "High"
            else -> "Very High"
        }
    }

    private fun getPotassiumColor(value: Float): String {
        return when {
            value < 50 -> "#FF0000"
            value < 100 -> "#FFA500"
            value < 200 -> "#00FF00"
            value < 300 -> "#FFFF00"
            else -> "#8A2BE2"
        }
    }

    private fun getNitrogenStatus(value: Float): String {
        return when {
            value < 0.1 -> "Very Low"
            value < 0.5 -> "Low"
            value < 1.0 -> "Normal"
            value < 2.0 -> "High"
            else -> "Very High"
        }
    }

    private fun getNitrogenColor(value: Float): String {
        return when {
            value < 0.1 -> "#FF0000"
            value < 0.5 -> "#FFA500"
            value < 1.0 -> "#00FF00"
            value < 2.0 -> "#FFFF00"
            else -> "#8A2BE2"
        }
    }

    private fun getHumidityStatus(value: Float): String {
        return when {
            value < 10 -> "Very Low"
            value < 25 -> "Low"
            value < 40 -> "Normal"
            value < 60 -> "High"
            else -> "Very High"
        }
    }

    private fun getHumidityColor(value: Float): String {
        return when {
            value < 10 -> "#FF0000"
            value < 25 -> "#FFA500"
            value < 40 -> "#00FF00"
            value < 60 -> "#FFFF00"
            else -> "#8A2BE2"
        }
    }


    private fun setupParameterChart(chart: HorizontalBarChart, parameter: String, value: Float, colorHex: String, status: String) {
        // Create entries for the chart
        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(0f, value))

        // Create dataset and customize appearance
        val dataSet = BarDataSet(entries, parameter)
        dataSet.color = Color.parseColor(colorHex)
        dataSet.setDrawValues(false)

        // Set data to chart
        val data = BarData(dataSet)
        data.barWidth = 0.5f
        chart.data = data

        // Update parameter labels in layout
        val parameterIndex = when (parameter) {
            "Soil Moisture" -> 0
            "pH Value" -> 1
            "Conductivity" -> 2
            "Phosphorus" -> 3
            "Potassium" -> 4
            "Nitrogen" -> 5
            "Temperature" -> 6
            else -> -1
        }

        if (parameterIndex >= 0) {
            val parameterNameView = binding.root.findViewById<TextView>(
                resources.getIdentifier("parameter_name_$parameterIndex", "id", requireContext().packageName)
            )
            val parameterStatusView = binding.root.findViewById<TextView>(
                resources.getIdentifier("parameter_status_$parameterIndex", "id", requireContext().packageName)
            )

            parameterNameView?.text = parameter
            parameterStatusView?.text = status
            parameterStatusView?.setTextColor(Color.parseColor(colorHex))
        }


        // Refresh chart
        chart.invalidate()
    }
}