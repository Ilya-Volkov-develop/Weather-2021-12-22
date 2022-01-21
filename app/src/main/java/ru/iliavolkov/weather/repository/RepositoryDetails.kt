package ru.iliavolkov.weather.repository

import retrofit2.Callback
import ru.iliavolkov.weather.model.WeatherDTO

interface RepositoryDetails {
    fun getWeatherFromServer(lat:Double, lon:Double,callBack: Callback<WeatherDTO>)
}