package ru.iliavolkov.weather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.iliavolkov.weather.model.Repository
import ru.iliavolkov.weather.model.RepositoryImpl
import ru.iliavolkov.weather.model.Weather
import java.lang.Exception
import java.lang.Thread.sleep

class MainViewModel(
        private val liveData: MutableLiveData<AppState> = MutableLiveData(),
        private val repositoryImpl: RepositoryImpl = RepositoryImpl()
): ViewModel() {

    fun getLiveData():LiveData<AppState>{
        return liveData
    }

    fun getWeatherFromServer(){
        Thread{
            liveData.postValue(AppState.Loading(0))
            sleep(3000)
            when((1..2).random()){
              1->liveData.postValue(AppState.Success(repositoryImpl.getWeatherFromServer()))
              2->liveData.postValue(AppState.Error("Ошибка"))
            }



        }.start()
    }

    fun getWeather() {
        getWeatherFromServer()
    }

}