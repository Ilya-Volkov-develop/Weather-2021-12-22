package ru.iliavolkov.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.iliavolkov.weather.repository.RepositoriesLocalCitiesImpl
import java.lang.Thread.sleep

class MainViewModel(private val liveData: MutableLiveData<AppStateCity> = MutableLiveData()): ViewModel() {

    private val repositoriesLocalCitiesImpl: RepositoriesLocalCitiesImpl by lazy {
        RepositoriesLocalCitiesImpl()
    }


    fun getLiveData() = liveData

    fun getWeatherFromLocalSourceRus() = getWeatherFromLocalServer(isRussian = true)

    fun getWeatherFromLocalSourceWorld() = getWeatherFromLocalServer(isRussian = false)

    fun getWeatherFromRemoteSource() = getWeatherFromLocalServer(isRussian = true)//заглука 5 урок

    private fun getWeatherFromLocalServer(isRussian:Boolean){
        Thread{
            liveData.postValue(AppStateCity.Loading(0))
            sleep(1000)
            liveData.postValue(AppStateCity.Success(
                repositoriesLocalCitiesImpl.run {
                        if (isRussian) getWeatherFromLocalStorageRus()
                        else getWeatherFromLocalStorageWorld()
                    }))
        }.start()
    }
}