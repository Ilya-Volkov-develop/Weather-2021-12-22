package ru.iliavolkov.weather.viewmodel

import ru.iliavolkov.weather.model.Weather

sealed class AppState {
    data class Loading(val progress:Int): AppState()
    data class Success(var weatherData:Weather): AppState()
    //data class Error(val error:Throwable): AppState()
    data class Error(val error:String): AppState()

}