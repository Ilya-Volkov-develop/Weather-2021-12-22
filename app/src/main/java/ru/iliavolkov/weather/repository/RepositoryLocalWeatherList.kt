package ru.iliavolkov.weather.repository

import ru.iliavolkov.weather.model.Weather

interface RepositoryLocalWeatherList {
    fun getWeatherFromLocalStorageRus(): List<Weather>
    fun getWeatherFromLocalStorageWorld(): List<Weather>
}