package ru.iliavolkov.weather.repository

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import ru.iliavolkov.weather.model.WeatherDTO
import ru.iliavolkov.weather.utils.API_KEY_NAME
import ru.iliavolkov.weather.utils.YANDEX_API_URL_END_POINT

interface WeatherApi {
    @GET(YANDEX_API_URL_END_POINT)
    fun getWeather(
        @Header(API_KEY_NAME) apikey:String,
        @Query("lat") lat:Double,
        @Query("lon") lon:Double
    ): Call<WeatherDTO>
}