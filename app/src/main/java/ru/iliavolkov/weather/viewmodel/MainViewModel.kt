package ru.iliavolkov.weather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.Thread.sleep

class MainViewModel(private val liveData: MutableLiveData<AppState> = MutableLiveData()): ViewModel() {

    fun getLiveData():LiveData<AppState>{
        return liveData
    }

    fun getWeatherFromServer(){
        Thread{
            liveData.postValue(AppState.Loading(0))
            sleep(3000)
            liveData.postValue(AppState.Success("Тепло"))
        }.start()
    }

}