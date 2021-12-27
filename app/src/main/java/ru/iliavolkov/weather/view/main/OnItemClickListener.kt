package ru.iliavolkov.weather.view.main

import ru.iliavolkov.weather.model.Weather

interface OnItemClickListener {
    fun onItemClick(weather: Weather)
}