package ru.iliavolkov.weather.viewmodel

import ru.iliavolkov.weather.model.Weather

sealed class AppStateWeather {
    data class Loading(val progress:Int): AppStateWeather()
    data class Success(
        var weatherData: Weather,
        val condition: String
        ): AppStateWeather()
    data class Error(val error:Int, val code:Int): AppStateWeather()

}