package ru.iliavolkov.weather.repository

import ru.iliavolkov.weather.model.Weather

interface RepositoryHistoryWeather {
    fun getAllHistoryWeather():List<Weather>
    fun saveWeather(weather: Weather)
}