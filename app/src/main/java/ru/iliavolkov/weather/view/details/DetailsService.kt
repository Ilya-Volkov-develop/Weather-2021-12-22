package ru.iliavolkov.weather.view.details

import android.app.IntentService
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import ru.iliavolkov.weather.BuildConfig
import ru.iliavolkov.weather.model.WeatherDTO
import ru.iliavolkov.weather.utils.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class DetailsService(name:String=""):IntentService(name) {
    override fun onHandleIntent(intent: Intent?) {
        intent?.let{
            loadWeather(
                intent.getDoubleExtra(KEY_LAT,0.0),
                intent.getDoubleExtra(KEY_LON,0.0)
            )
        }
    }

    fun loadWeather(lat:Double, lon:Double){

        val url = URL("https://api.weather.yandex.ru/v2/informers?lat=$lat&lon=$lon")
        val httpsURLConnection = (url.openConnection() as HttpsURLConnection).apply {
            requestMethod = "GET"
            readTimeout = 2000
            addRequestProperty(API_KEY_NAME, BuildConfig.WEATHER_API_KEY)
        }
        val bufferedReader = BufferedReader(InputStreamReader(httpsURLConnection.inputStream))
        val weatherDTO: WeatherDTO? = Gson().fromJson(convertBufferToResult(bufferedReader), WeatherDTO::class.java)
        LocalBroadcastManager.getInstance(applicationContext).
            sendBroadcast(Intent(BROADCAST_ACTION).apply {
                putExtra(BUNDLE_KEY_WEATHER,weatherDTO)
            })
        httpsURLConnection.disconnect()
    }

    private fun convertBufferToResult(buffer: BufferedReader):String {
        return buffer.lines().collect(Collectors.joining("\n"))
    }
}