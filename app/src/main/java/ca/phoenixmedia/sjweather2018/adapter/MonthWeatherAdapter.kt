package ca.phoenixmedia.sjweather2018.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ca.phoenixmedia.sjweather2018.App
import ca.phoenixmedia.sjweather2018.R
import ca.phoenixmedia.sjweather2018.model.MonthWeather

class MonthWeatherAdapter(private val monthWeatherList: List<MonthWeather>) : RecyclerView.Adapter<MonthWeatherAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val header: TextView = view.findViewById(R.id.header)
        val minTemp: TextView = view.findViewById(R.id.min_temp)
        val avgTemp: TextView = view.findViewById(R.id.avg_temp)
        val maxTemp: TextView = view.findViewById(R.id.max_temp)
        val totalPrecipitation: TextView = view.findViewById(R.id.total_precipitation)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.month_weather_row_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.header.text = monthWeatherList[position].monthString
        viewHolder.minTemp.text = App.applicationContext().getString(
            R.string.min_temp,
            monthWeatherList[position].minTempC.toString()
        )
        viewHolder.avgTemp.text = App.applicationContext().getString(
            R.string.avg_temp,
            monthWeatherList[position].meanTempC.toString()
        )
        viewHolder.maxTemp.text = App.applicationContext().getString(
            R.string.max_temp,
            monthWeatherList[position].maxTempC.toString()
        )
        viewHolder.totalPrecipitation.text = App.applicationContext().getString(
            R.string.total_precipitation,
            monthWeatherList[position].totalPrecipMm.toString()
        )
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = monthWeatherList.size
}