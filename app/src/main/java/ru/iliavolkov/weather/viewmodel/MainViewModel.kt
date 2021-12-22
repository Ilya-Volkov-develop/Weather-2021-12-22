package ru.iliavolkov.weather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel(val liveData: MutableLiveData<Any> = MutableLiveData()): ViewModel() {

    fun getLiveData():LiveData<Any>{
        return liveData
    }

}