package ca.phoenixmedia.sjweather2018.model

import java.math.RoundingMode
import java.text.DateFormatSymbols

class MonthWeather(weatherItems: List<WeatherItem>) {
    var minTempC = 0.0
    var meanTempC = 0.0
    var maxTempC = 0.0
    var totalPrecipMm = 0.0
    var monthString: String
    var month: Int = 0

    init {
        var itemCount = 0
        var meanTempTotal = 0.0

        weatherItems.forEach {
            if (it.minTempC < minTempC)
                minTempC = it.minTempC

            if (it.maxTempC > maxTempC)
                maxTempC = it.maxTempC

            meanTempTotal += it.meanTempC

            totalPrecipMm += it.totalPrecipMm

            itemCount++
        }

        meanTempC = (meanTempTotal / itemCount).toBigDecimal().setScale(1, RoundingMode.HALF_UP).toDouble()
        totalPrecipMm = totalPrecipMm.toBigDecimal().setScale(1, RoundingMode.HALF_UP).toDouble()

        monthString = DateFormatSymbols().months[weatherItems[0].month-1]
        month = weatherItems[0].month
    }
}