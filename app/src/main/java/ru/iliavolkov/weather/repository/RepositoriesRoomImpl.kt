package ru.iliavolkov.weather.repository

import ru.iliavolkov.weather.model.City
import ru.iliavolkov.weather.model.Weather
import ru.iliavolkov.weather.room.App
import ru.iliavolkov.weather.room.HistoryWeatherEntity

class RepositoriesRoomImpl:RepositoryHistoryWeather {

    override fun getAllHistoryWeather(): List<Weather> {
        return convertHistoryWeatherEntityToWeather(App.getHistoryWeatherDao().getAllHistoryWeather())
    }

    private fun convertHistoryWeatherEntityToWeather(entityList:List<HistoryWeatherEntity>):List<Weather>{
        return entityList.map{
            Weather(
                City(it.city,0.0,0.0),
                it.temperature,
                it.feelsLike
            )
        }
    }

    override fun saveWeather(position:Int,weather: Weather) {
        Thread {
            App.getHistoryWeatherDao().insert(
                convertWeatherToHistoryWeatherEntity(position, weather)
            )
        }.start()
    }

    override fun delete(weather: Weather) {
        App.getHistoryWeatherDao().delete(
                convertWeatherToHistoryWeatherEntity(weather)
        )
    }

    private fun convertWeatherToHistoryWeatherEntity(weather: Weather): HistoryWeatherEntity {
        return HistoryWeatherEntity(
            0,
            weather.city.nameCity,
            weather.temperature,
            weather.feelsLike
        )
    }
}