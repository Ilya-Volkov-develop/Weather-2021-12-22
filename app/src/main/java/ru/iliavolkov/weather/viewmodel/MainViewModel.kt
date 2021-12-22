package ru.iliavolkov.weather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.Exception
import java.lang.Thread.sleep

class MainViewModel(private val liveData: MutableLiveData<AppState> = MutableLiveData()): ViewModel() {

    fun getLiveData():LiveData<AppState>{
        return liveData
    }

    fun getWeatherFromServer(){
        Thread{
            liveData.postValue(AppState.Loading(0))
            sleep(3000)
            when((1..2).random()){
              1->liveData.postValue(AppState.Success("Тепло"))
              2->liveData.postValue(AppState.Error("Ошибка"))
            }



        }.start()
    }

}