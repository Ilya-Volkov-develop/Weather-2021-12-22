package ru.iliavolkov.weather.repository

import ru.iliavolkov.weather.model.getRussianCities
import ru.iliavolkov.weather.model.getWorldCities

class RepositoriesLocalCitiesImpl: RepositoryLocalWeatherList{

    override fun getWeatherFromLocalStorageRus() = getRussianCities()

    override fun getWeatherFromLocalStorageWorld() = getWorldCities()
}