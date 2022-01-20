package ru.iliavolkov.weather.repository

import okhttp3.Callback

interface RepositoryDetails {
    fun getWeatherFromServer(url:String,callBack: Callback)
}