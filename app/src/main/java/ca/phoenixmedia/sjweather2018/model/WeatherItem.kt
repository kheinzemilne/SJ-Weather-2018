package ca.phoenixmedia.sjweather2018.model

import com.squareup.moshi.Json

data class WeatherItem(
    @field:Json(name = "Year") val year: Int,
    @field:Json(name = "Month") val month: Int,
    @field:Json(name = "Day") val day: Int,
    @field:Json(name = "Max Temp (°C)") val maxTempC: Double,
    @field:Json(name = "Min Temp (°C)") val minTempC: Double,
    @field:Json(name = "Mean Temp (°C)") val meanTempC: Double,
    @field:Json(name = "Total Precip (mm)") val totalPrecipMm: Double
)