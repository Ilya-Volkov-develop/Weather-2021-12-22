package ru.iliavolkov.weather.view.main

import ru.iliavolkov.weather.model.City

interface OnItemClickListener {
    fun onItemClick(city: City,position: Int)
}