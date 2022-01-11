package ru.iliavolkov.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.iliavolkov.weather.model.RepositoryImpl
import java.lang.Thread.sleep

class MainViewModel(
        private val liveData: MutableLiveData<AppState> = MutableLiveData(),
        private val repositoryImpl:RepositoryImpl = RepositoryImpl()
): ViewModel() {

    fun getLiveData() = liveData

    fun getWeatherFromLocalSourceRus() = getWeatherFromLocalServer(isRussian = true)

    fun getWeatherFromLocalSourceWorld() = getWeatherFromLocalServer(isRussian = false)

    fun getWeatherFromRemoteSource() = getWeatherFromLocalServer(isRussian = true)//заглука 5 урок

    fun getWeatherFromLocalServer(isRussian:Boolean){
        Thread{
            liveData.postValue(AppState.Loading(0))
            sleep(1000)
            liveData.postValue(AppState.Success(
                    repositoryImpl.run {
                        if (isRussian) getWeatherFromLocalStorageRus()
                        else getWeatherFromLocalStorageWorld()
                    }))
        }.start()
    }
}