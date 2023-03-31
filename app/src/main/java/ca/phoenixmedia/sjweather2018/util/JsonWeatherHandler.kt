package ca.phoenixmedia.sjweather2018.util

import android.util.Log
import ca.phoenixmedia.sjweather2018.App
import ca.phoenixmedia.sjweather2018.model.MonthWeather
import ca.phoenixmedia.sjweather2018.model.WeatherItem
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader
import com.squareup.moshi.Moshi
import okio.Buffer
import java.io.IOException
import kotlin.text.Charsets.UTF_8

class JsonWeatherHandler {
    companion object {
        fun createMonthWeatherList() : List<MonthWeather> {
            val monthWeatherList = mutableListOf<MonthWeather>()
            val weatherItems = getWeatherItemsFromJson()

            if (weatherItems != null) {
                for (i in 1..12) {
                    monthWeatherList.add(MonthWeather(weatherItems.filter { it.month == i }))
                }
            }

            return monthWeatherList
        }

        private fun getWeatherItemsFromJson(): List<WeatherItem>? {
            lateinit var jsonString: String
            try {
                jsonString = App.applicationContext().assets.open("EnvCanada-2018-daily-weather-data.json")
                    .bufferedReader()
                    .use { it.readText() }
            } catch (ioException: IOException) {
                Log.d("Reading weather file failed.", ioException.message ?: "No error message found.")
            }

            // Prefer to use Moshi's automatic parsing as it is faster and less prone to mistakes
            var weatherItems = DataUnwrapper.fromJsonList(
                Moshi.Builder().build(),
                WeatherItem::class.java,
                jsonString
            ) as? List<WeatherItem>

            // If the automatic parsing doesn't work, switch to manual in order to salvage
            // Whatever good data is available while discarding malformed or incomplete entries
            if (weatherItems == null || weatherItems.isEmpty()) {
                Log.d("JsonWeatherHandler", "Automatic parsing failed.")
                // If Moshi couldn't automatically parse the JSON string, use manual parsing
                weatherItems = parseMalformedJson(jsonString)
            }

            return weatherItems
        }

        private fun parseMalformedJson(json: String): List<WeatherItem> {
            val weatherItems = mutableListOf<WeatherItem>()

            val reader = JsonReader.of(Buffer().writeString(json, UTF_8))

            reader.beginArray()

            while (reader.hasNext()) {
                val item = readWeatherItem(reader)
                if (item != null)
                    weatherItems.add(item)
            }

            reader.endArray()

            return weatherItems
        }

        private fun readWeatherItem(reader: JsonReader): WeatherItem? {
            var year: Int? = null
            var month: Int? = null
            var day: Int? = null
            var maxTempC: Double? = null
            var minTempC: Double? = null
            var meanTempC: Double? = null
            var totalPrecipMm: Double? = null
            var badData = false

            reader.beginObject()

            while (reader.hasNext()) {
                // If we already have all our values, or we already know the data is bad,
                // we don't need to keep parsing the JSON.
                if (
                    (
                        year != null
                        && month != null
                        && day != null
                        && maxTempC != null
                        && minTempC != null
                        && meanTempC != null
                        && totalPrecipMm != null
                    ) || badData
                ) {
                    reader.skipName()
                    reader.skipValue()
                } else {
                    // Try to parse the values we are looking for, if we get missing or malformed
                    // data, flag the object as bad data so we can skip parsing the rest of it and
                    // discard it.
                    try {
                        when (reader.nextName()) {
                            "Year" -> year = reader.nextInt()
                            // Given more time, would like to implement validation for months and days to ensure
                            // the values make sense.
                            "Month" -> month = reader.nextInt()
                            "Day" -> day = reader.nextInt()
                            "Max Temp (°C)" -> maxTempC = reader.nextDouble()
                            "Min Temp (°C)" -> minTempC = reader.nextDouble()
                            "Mean Temp (°C)" -> meanTempC = reader.nextDouble()
                            "Total Precip (mm)" -> totalPrecipMm = reader.nextDouble()
                            else -> reader.skipValue()
                        }
                    } catch (ex: Exception) {
                        when (ex) {
                            is IOException, is JsonDataException ->
                            {
                                Log.d("ReadWeatherItem", "Failed to parse json, discarding day.")
                                reader.skipValue()
                                badData = true
                            }
                        }
                    }
                }
            }

            reader.endObject()

            return if (
                year != null
                && month != null
                && day != null
                && maxTempC != null
                && minTempC != null
                && meanTempC != null
                && totalPrecipMm != null
            ) {
                WeatherItem(year, month, day, maxTempC, minTempC, meanTempC, totalPrecipMm)
            } else {
                Log.d("ReadWeatherItem", "Incomplete data for day, discarding.")
                null
            }
        }
    }
}