package ru.iliavolkov.weather.view.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ru.iliavolkov.weather.R
import ru.iliavolkov.weather.model.Weather

class MainFragmentAdapter: RecyclerView.Adapter<MainFragmentAdapter.MainViewHolder>() {

    private var weatherData:List<Weather> = listOf()

    fun setWeather(data:List<Weather>){
        this.weatherData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.main_recycler_item,parent,false))
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(this.weatherData[position])
    }

    override fun getItemCount(): Int {
        return weatherData.size
    }

    class MainViewHolder(view:View):RecyclerView.ViewHolder(view){
        fun bind(weather: Weather){
            itemView.findViewById<TextView>(R.id.mainFragmentRecyclerItemTextView).text = weather.city.nameCity
            itemView.setOnClickListener{
                Toast.makeText(itemView.context,"sdf",Toast.LENGTH_SHORT).show()
            }

        }
    }
}