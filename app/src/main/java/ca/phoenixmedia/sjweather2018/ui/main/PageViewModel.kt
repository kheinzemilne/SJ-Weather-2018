package ca.phoenixmedia.sjweather2018.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ca.phoenixmedia.sjweather2018.model.MonthWeather

class PageViewModel : ViewModel() {

    private val _index = MutableLiveData<Int>()

    val monthWeatherList = MutableLiveData<List<MonthWeather>>()

    fun setIndex(index: Int) {
        _index.value = index
    }
}