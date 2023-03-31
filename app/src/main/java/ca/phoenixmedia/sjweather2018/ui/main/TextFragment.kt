package ca.phoenixmedia.sjweather2018.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ca.phoenixmedia.sjweather2018.MonthWeatherAdapter
import ca.phoenixmedia.sjweather2018.databinding.FragmentTextBinding
import ca.phoenixmedia.sjweather2018.model.MonthWeather
import ca.phoenixmedia.sjweather2018.util.JsonWeatherHandler

/**
 * A placeholder fragment containing a simple view.
 */
class TextFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    private var _binding: FragmentTextBinding? = null

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

        _binding = FragmentTextBinding.inflate(inflater, container, false)
        val root = binding.root

        pageViewModel.monthWeatherList.value = JsonWeatherHandler.createMonthWeatherList()

        pageViewModel.monthWeatherList.observe(viewLifecycleOwner) {
            createWeatherSummaries(it)
        }

        return root
    }

    private fun createWeatherSummaries(monthWeatherList: List<MonthWeather>) {
        val monthWeatherRecyclerView: RecyclerView = binding.monthWeatherRecycler
        monthWeatherRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        monthWeatherRecyclerView.adapter = MonthWeatherAdapter(monthWeatherList)
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
        fun newInstance(sectionNumber: Int): TextFragment {
            return TextFragment().apply {
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