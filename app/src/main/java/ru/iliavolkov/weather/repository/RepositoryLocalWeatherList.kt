package ru.iliavolkov.weather.repository

import ru.iliavolkov.weather.model.City

interface RepositoryLocalWeatherList {
    fun getWeatherFromLocalStorageRus(): List<City>
    fun getWeatherFromLocalStorageWorld(): List<City>
}