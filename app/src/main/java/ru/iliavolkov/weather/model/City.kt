package ru.iliavolkov.weather.model

data class City(val nameCity:String, val lon:Double, val lat:Double)

fun getDefaultCity() = City("Москва",55.755826,37.617299900000035)
