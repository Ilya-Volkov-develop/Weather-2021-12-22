package ru.iliavolkov.weather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.iliavolkov.weather.model.RepositoryImpl
import java.lang.Thread.sleep

class MainViewModel(
        private val liveData: MutableLiveData<AppState> = MutableLiveData(),
        private val repositoryImpl: RepositoryImpl = RepositoryImpl()
): ViewModel() {

    fun getLiveData():LiveData<AppState>{
        return liveData
    }

    fun getWeatherFromLocalSourceRus() = getWeatherFromLocalServer(isRussian = true)

    fun getWeatherFromLocalSourceWorld() = getWeatherFromLocalServer(isRussian = false)

    fun getWeatherFromRemoteSource() = getWeatherFromLocalServer(isRussian = true)//заглука 5 урок

    fun getWeatherFromLocalServer(isRussian:Boolean){
        Thread{
            liveData.postValue(AppState.Loading(0))
            sleep(3000)
//            when((1..2).random()){
//              1->liveData.postValue(AppState.Success(repositoryImpl.getWeatherFromServer()))
//              2->liveData.postValue(AppState.Error("Ошибка"))
//            }
            liveData.postValue(AppState.Success(
                    if (isRussian) repositoryImpl.getWeatherFromLocalStorageRus()
                    else repositoryImpl.getWeatherFromLocalStorageWorld()))



        }.start()
    }

    fun getWeather() {
        getWeatherFromLocalServer()
    }

}