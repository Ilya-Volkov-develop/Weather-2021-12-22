package ru.iliavolkov.weather.view.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.iliavolkov.weather.R
import ru.iliavolkov.weather.databinding.MainRecyclerItemBinding
import ru.iliavolkov.weather.model.Weather

class MainFragmentAdapter(val listener: OnItemClickListener): RecyclerView.Adapter<MainFragmentAdapter.MainViewHolder>() {

    private var weatherData:List<Weather> = listOf()

    fun setWeather(data:List<Weather>){
        this.weatherData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_main_recycler_city_item,parent,false))
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(this.weatherData[position])
    }

    override fun getItemCount(): Int {
        return weatherData.size
    }

    inner class MainViewHolder(view:View):RecyclerView.ViewHolder(view){
        fun bind(weather: Weather){
            MainRecyclerItemBinding.bind(itemView).run {
                mainFragmentRecyclerItemTextView.text = weather.city.nameCity
                root.setOnClickListener{ listener.onItemClick(weather) }
            }


        }
    }
}