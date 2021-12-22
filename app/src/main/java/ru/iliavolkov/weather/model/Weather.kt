package ru.iliavolkov.weather.model

data class Weather(val city:City = getDefaultCity(), val temperature:Double = 20.0, val feelsLike:Double = 20.0)

fun getDefaultCity() = City("Москва",55.755826,37.617299900000035)


