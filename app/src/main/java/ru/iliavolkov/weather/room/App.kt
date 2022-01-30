package ru.iliavolkov.weather.room

import android.app.Application
import androidx.room.Room
import ru.iliavolkov.weather.utils.DB_NAME
import java.util.*

class App:Application() {
    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    companion object{
        private var appInstance:App? = null
        private var db:HistoryDatabase? = null

        fun getHistoryWeatherDao():HistoryWeatherDAO{
            if (db==null){
                if (appInstance==null){
                    throw IllformedLocaleException("Всё плохо")
                } else{
                    db = Room.databaseBuilder(appInstance!!.applicationContext,HistoryDatabase::class.java, DB_NAME)
                        .build()
                }
            }
            return db!!.historyWeatherDao()
        }
    }
}