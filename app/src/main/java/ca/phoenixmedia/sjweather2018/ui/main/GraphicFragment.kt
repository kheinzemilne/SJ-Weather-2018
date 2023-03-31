package ca.phoenixmedia.sjweather2018.ui.main

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ca.phoenixmedia.sjweather2018.databinding.FragmentGraphicBinding
import ca.phoenixmedia.sjweather2018.model.MonthWeather
import ca.phoenixmedia.sjweather2018.util.JsonWeatherHandler
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class GraphicFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    private var _binding: FragmentGraphicBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(this)[PageViewModel::class.java].apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGraphicBinding.inflate(inflater, container, false)
        val root = binding.root

        pageViewModel.monthWeatherList.value = JsonWeatherHandler.createMonthWeatherList()

        pageViewModel.monthWeatherList.observe(viewLifecycleOwner) {
            setLineChartData(it)
        }
        return root
    }

    private fun setLineChartData(monthWeatherList: List<MonthWeather>) {
        val dataSets = ArrayList<ILineDataSet>()

        val minValues = ArrayList<Entry>()
        monthWeatherList.forEach {
            minValues.add(Entry(it.month.toFloat(), it.minTempC.toFloat()))
        }

        val maxValues = ArrayList<Entry>()
        monthWeatherList.forEach {
            maxValues.add(Entry(it.month.toFloat(), it.maxTempC.toFloat()))
        }

        val avgValues = ArrayList<Entry>()
        monthWeatherList.forEach {
            avgValues.add(Entry(it.month.toFloat(), it.meanTempC.toFloat()))
        }

        val totalPrecipitation = ArrayList<Entry>()
        monthWeatherList.forEach {
            totalPrecipitation.add(Entry(it.month.toFloat(), it.totalPrecipMm.toFloat()))
        }

        dataSets.add(createDataSet(minValues, Color.BLUE, "Min Temp (°C)"))
        dataSets.add(createDataSet(avgValues, Color.YELLOW, "Avg Temp (°C)"))
        dataSets.add(createDataSet(maxValues, Color.RED, "Max Temp (°C)"))
        dataSets.add(createDataSet(totalPrecipitation, Color.GRAY, "Total Precip (mm)"))

        val data = LineData(dataSets)
        binding.weatherGraph.data = data
        binding.weatherGraph.setBackgroundColor(Color.WHITE)
        binding.weatherGraph.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.weatherGraph.description.isEnabled = true
        binding.weatherGraph.description.text = "Weather Data by Month"
        binding.weatherGraph.description.textSize = 20f
        binding.weatherGraph.animateXY(2000, 2000, Easing.EaseInCubic)
    }

    private fun createDataSet(data: ArrayList<Entry>, color: Int, label: String): ILineDataSet {
        val dataSet = LineDataSet(data, label)
        dataSet.color = color

        dataSet.circleRadius = 4f
        dataSet.setDrawFilled(false)
        dataSet.valueTextSize = 11f
        dataSet.mode = LineDataSet.Mode.LINEAR

        return dataSet
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): GraphicFragment {
            return GraphicFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}