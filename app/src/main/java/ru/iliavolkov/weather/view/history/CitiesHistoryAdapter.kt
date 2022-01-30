package ru.iliavolkov.weather.view.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import ru.iliavolkov.weather.R
import ru.iliavolkov.weather.databinding.FragmentHistoryRecyclerCityItemBinding
import ru.iliavolkov.weather.model.Weather

class CitiesHistoryAdapter: RecyclerView.Adapter<CitiesHistoryAdapter.CitiesHistoryHolder>() {

    private var weatherData:List<Weather> = listOf()

    fun setWeather(data:List<Weather>){
        this.weatherData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesHistoryHolder {
        return CitiesHistoryHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_history_recycler_city_item,parent,false))
    }

    override fun onBindViewHolder(holder: CitiesHistoryHolder, position: Int) {
        holder.bind(this.weatherData[position])
    }

    override fun getItemCount(): Int {
        return weatherData.size
    }

    inner class CitiesHistoryHolder(view: View): RecyclerView.ViewHolder(view){
        fun bind(weather: Weather){
            FragmentHistoryRecyclerCityItemBinding.bind(itemView).run {
                cityName.text = weather.city.nameCity
                temperature.text = weather.temperature.toString()
                feelsLike.text = weather.feelsLike.toString()
                iconWeather.loadIconSvg("https://yastatic.net/weather/i/icons/funky/dark/${weather.icon}.svg")
            }
        }

        private fun ImageView.loadIconSvg(url:String){
            val imageLoader = ImageLoader.Builder(this.context)
                .componentRegistry{add(SvgDecoder(this@loadIconSvg.context))}
                .build()

            val request = ImageRequest.Builder(this.context)
                .data(url)
                .target(this)
                .build()
            imageLoader.enqueue(request)
        }
    }
}