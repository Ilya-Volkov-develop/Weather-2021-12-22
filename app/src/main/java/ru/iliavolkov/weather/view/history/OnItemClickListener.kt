package ru.iliavolkov.weather.view.history

import ru.iliavolkov.weather.model.Weather

interface OnItemClickListener {
    fun onItemClick(weather: Weather)
}