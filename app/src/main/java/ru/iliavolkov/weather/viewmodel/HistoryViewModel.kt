package ru.iliavolkov.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.iliavolkov.weather.model.Weather
import ru.iliavolkov.weather.repository.RepositoriesRoomImpl

class HistoryViewModel(private val liveData: MutableLiveData<AppStateBD> = MutableLiveData()): ViewModel() {

    private val repositoriesRoomImpl: RepositoriesRoomImpl by lazy {
        RepositoriesRoomImpl()
    }

    fun getLiveData() = liveData

    fun getAllHistory(){
        val listWeather = repositoriesRoomImpl.getAllHistoryWeather()
        liveData.postValue(AppStateBD.Success(listWeather))
    }

    fun delete(weather:Weather){
        repositoriesRoomImpl.delete(weather)
        val listWeather = repositoriesRoomImpl.getAllHistoryWeather()
        liveData.postValue(AppStateBD.Success(listWeather))
    }
}