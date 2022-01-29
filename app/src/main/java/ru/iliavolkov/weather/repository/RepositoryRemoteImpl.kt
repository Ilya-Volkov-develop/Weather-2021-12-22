package ru.iliavolkov.weather.repository

import com.google.gson.GsonBuilder
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.iliavolkov.weather.BuildConfig
import ru.iliavolkov.weather.model.WeatherDTO
import ru.iliavolkov.weather.utils.YANDEX_API_URL

class RepositoryRemoteImpl: RepositoryDetails {

    private val retrofit = Retrofit.Builder()
        .baseUrl(YANDEX_API_URL)
        .addConverterFactory(GsonConverterFactory.create(
            GsonBuilder().setLenient().create()
        ))
        .build().create(WeatherApi::class.java)

    override fun getWeatherFromServer(lat:Double, lon:Double, callBack: Callback<WeatherDTO>) {
        retrofit.getWeather(BuildConfig.WEATHER_API_KEY,lat,lon).enqueue(callBack)
    }

}