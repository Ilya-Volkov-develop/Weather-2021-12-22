package ru.iliavolkov.weather.viewmodel

import ru.iliavolkov.weather.model.Weather

sealed class AppStateBD {
    data class Loading(val progress:Int): AppStateBD()
    data class Success(var weatherInfoHistoryData:List<Weather>): AppStateBD()
    data class Error(val error:String): AppStateBD()

}