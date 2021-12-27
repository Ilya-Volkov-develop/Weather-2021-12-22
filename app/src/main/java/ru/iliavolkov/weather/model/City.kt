package ru.iliavolkov.weather.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class City(val nameCity:String, val lon:Double, val lat:Double): Parcelable
