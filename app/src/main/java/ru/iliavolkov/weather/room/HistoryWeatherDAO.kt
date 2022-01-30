package ru.iliavolkov.weather.room

import androidx.room.*

@Dao
interface HistoryWeatherDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: HistoryWeatherEntity)

    @Delete
    fun delete(entity: HistoryWeatherEntity)

    @Update
    fun update(entity: HistoryWeatherEntity)

    @Query("select * FROM history_weather_entity")
    fun getAllHistoryWeather():List<HistoryWeatherEntity>
}