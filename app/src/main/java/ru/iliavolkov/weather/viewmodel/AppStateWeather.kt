package ru.iliavolkov.weather.viewmodel

import ru.iliavolkov.weather.model.Weather

sealed class AppStateWeather {
    data class Loading(val progress:Int): AppStateWeather()
    data class Success(var weatherData:Weather): AppStateWeather()
    data class Error(val error:String): AppStateWeather()

}