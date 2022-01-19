package ru.iliavolkov.weather.utils

import android.os.Handler
import android.os.Looper
import com.google.gson.Gson
import ru.iliavolkov.weather.BuildConfig
import ru.iliavolkov.weather.model.WeatherDTO
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class WeatherLoader(private val onWeatherLoaded:OnWeatherLoader) {

    fun loadWeather(lat:Double, lon:Double){
        val url = URL("https://api.weather.yandex.ru/v2/informers?lat=$lat&lon=$lon")
        val httpsURLConnection = (url.openConnection() as HttpsURLConnection).apply {
            requestMethod = "GET"
            readTimeout = 2000
            addRequestProperty(API_KEY, BuildConfig.WEATHER_API_KEY)
        }
        Thread {
            try {
                val bufferedReader =
                        BufferedReader(InputStreamReader(httpsURLConnection.inputStream))
                val weatherDTO: WeatherDTO? =
                        Gson().fromJson(convertBufferToResult(bufferedReader), WeatherDTO::class.java)
                Handler(Looper.getMainLooper()).post {
                    onWeatherLoaded.onLoaded(weatherDTO)
                }
            } catch (e:Exception){
                Handler(Looper.getMainLooper()).post {
                    onWeatherLoaded.onFailed()
                }
            } finally {
                httpsURLConnection.disconnect()
            }
        }.start()
    }


    private fun convertBufferToResult(buffer: BufferedReader):String {
        return buffer.lines().collect(Collectors.joining("\n"))
    }

    interface OnWeatherLoader{
        fun onLoaded(weatherDTO: WeatherDTO?)
        fun onFailed()
    }
}