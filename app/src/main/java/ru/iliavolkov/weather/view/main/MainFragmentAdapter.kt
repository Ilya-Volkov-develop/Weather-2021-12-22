package ru.iliavolkov.weather.view.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.iliavolkov.weather.R
import ru.iliavolkov.weather.databinding.FragmentMainRecyclerCityItemBinding
import ru.iliavolkov.weather.model.City

class MainFragmentAdapter(val listener: OnItemClickListener): RecyclerView.Adapter<MainFragmentAdapter.MainViewHolder>() {

    private var cityData:List<City> = listOf()

    fun setWeather(data:List<City>){
        this.cityData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_main_recycler_city_item,parent,false))
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(this.cityData[position],position)
    }

    override fun getItemCount(): Int {
        return cityData.size
    }

    inner class MainViewHolder(view:View):RecyclerView.ViewHolder(view){
        fun bind(city: City,position:Int){
            FragmentMainRecyclerCityItemBinding.bind(itemView).run {
                mainFragmentRecyclerItemTextView.text = city.nameCity
                root.setOnClickListener{ listener.onItemClick(city,position) }
            }


        }
    }
}