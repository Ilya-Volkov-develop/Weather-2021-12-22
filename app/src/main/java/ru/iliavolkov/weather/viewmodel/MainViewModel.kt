package ru.iliavolkov.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.iliavolkov.weather.repository.RepositoryImpl
import java.lang.Thread.sleep

class MainViewModel(private val liveData: MutableLiveData<AppStateCity> = MutableLiveData()): ViewModel() {

    private val repositoryImpl: RepositoryImpl by lazy {
        RepositoryImpl()
    }


    fun getLiveData() = liveData

    fun getWeatherFromLocalSourceRus() = getWeatherFromLocalServer(isRussian = true)

    fun getWeatherFromLocalSourceWorld() = getWeatherFromLocalServer(isRussian = false)

    fun getWeatherFromRemoteSource() = getWeatherFromLocalServer(isRussian = true)//заглука 5 урок

    fun getWeatherFromLocalServer(isRussian:Boolean){
        Thread{
            liveData.postValue(AppStateCity.Loading(0))
            sleep(1000)
            liveData.postValue(AppStateCity.Success(
                    repositoryImpl.run {
                        if (isRussian) getWeatherFromLocalStorageRus()
                        else getWeatherFromLocalStorageWorld()
                    }))
        }.start()
    }
}