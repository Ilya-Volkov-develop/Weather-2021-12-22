package ru.iliavolkov.weather.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [HistoryWeatherEntity::class], version = 1, exportSchema = false)
abstract class HistoryDatabase:RoomDatabase() {
    abstract fun historyWeatherDao():HistoryWeatherDAO
}