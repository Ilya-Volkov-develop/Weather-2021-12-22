package ru.iliavolkov.weather.repository

import ru.iliavolkov.weather.model.City
import ru.iliavolkov.weather.model.Weather

interface RepositoryLocalWeatherList {
    fun getWeatherFromLocalStorageRus(): List<City>
    fun getWeatherFromLocalStorageWorld(): List<City>
}