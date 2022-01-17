package ru.iliavolkov.weather.utils

import com.google.gson.Gson
import ru.iliavolkov.weather.model.WeatherDTO
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class WeatherLoader(private val lat:Double, private val lon:Double,private val onWeatherLoaded:OnWeatherLoader) {

    fun loadWeather(){
        val url = URL("https://api.weather.yandex.ru/v2/informers?lat=$lat&lon=$lon")
        val httpsURLConnection = (url.openConnection() as HttpsURLConnection).apply {
            requestMethod = "GET"
            readTimeout = 5000
            addRequestProperty("X-Yandex-API-Key","f8120338-96d7-4f36-84c6-173ddb32eaa2")
        }
        val bufferedReader = BufferedReader(InputStreamReader(httpsURLConnection.inputStream))
        val weatherDTO = Gson().fromJson(convertBufferToResult(bufferedReader),WeatherDTO::class.java)
        onWeatherLoaded.onLoaded(weatherDTO)
    }


    private fun convertBufferToResult(buffer: BufferedReader):String {
        return buffer.lines().collect(Collectors.joining("\n"))
    }

    interface OnWeatherLoader{
        fun onLoaded(weatherDTO: WeatherDTO)
        fun onFailed()
    }
}