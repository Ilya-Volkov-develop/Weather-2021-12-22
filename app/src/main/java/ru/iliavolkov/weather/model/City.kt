package ru.iliavolkov.weather.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class City(val nameCity:String, val lat:Double, val lon:Double): Parcelable
