package ru.iliavolkov.weather.view.details

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ru.iliavolkov.weather.model.WeatherDTO
import ru.iliavolkov.weather.utils.BUNDLE_KEY_WEATHER

class DetailsReceiver(private val receiverTotal: ReceiverTotal):BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        intent?.let {
            val weatherDTO = it.getParcelableExtra<WeatherDTO>(BUNDLE_KEY_WEATHER)
            if (weatherDTO != null){
                receiverTotal.success(weatherDTO)
            } else{
                receiverTotal.error()
            }
        }
    }
    interface ReceiverTotal{
        fun success(weatherDTO: WeatherDTO)
        fun error()
    }
}
