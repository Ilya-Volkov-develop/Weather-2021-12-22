package ru.iliavolkov.weather.viewmodel

import ru.iliavolkov.weather.model.City

sealed class AppStateCity {
    data class Loading(val progress:Int): AppStateCity()
    data class Success(var cityData:List<City>): AppStateCity()
    data class Error(val error:String): AppStateCity()

}