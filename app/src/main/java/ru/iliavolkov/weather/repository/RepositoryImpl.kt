package ru.iliavolkov.weather.repository

import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import ru.iliavolkov.weather.BuildConfig
import ru.iliavolkov.weather.model.getRussianCities
import ru.iliavolkov.weather.model.getWorldCities
import ru.iliavolkov.weather.utils.API_KEY_NAME

class RepositoryImpl: RepositoryLocalWeatherList,RepositoryDetails {

    override fun getWeatherFromLocalStorageRus() = getRussianCities()

    override fun getWeatherFromLocalStorageWorld() = getWorldCities()

    override fun getWeatherFromServer(url: String, callBack: Callback) {
        val builder = Request.Builder().apply {
            header(API_KEY_NAME,BuildConfig.WEATHER_API_KEY)
            url(url)
        }
        OkHttpClient().newCall(builder.build()).enqueue(callBack)
    }

}