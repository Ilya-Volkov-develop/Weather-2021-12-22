package ru.iliavolkov.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.iliavolkov.weather.R
import ru.iliavolkov.weather.model.Weather
import ru.iliavolkov.weather.model.WeatherDTO
import ru.iliavolkov.weather.model.getDefaultCity
import ru.iliavolkov.weather.repository.RepositoriesRoomImpl
import ru.iliavolkov.weather.repository.RepositoryRemoteImpl

class DetailsViewModel(
    private val liveData: MutableLiveData<AppStateWeather> = MutableLiveData()): ViewModel() {

    private val repositoryRemoteImpl: RepositoryRemoteImpl by lazy {
        RepositoryRemoteImpl()
    }

    private val repositoriesRoomImpl: RepositoriesRoomImpl by lazy {
        RepositoriesRoomImpl()
    }

    fun getLiveData() = liveData

    fun saveWeather(position:Int, weather: Weather){
        repositoriesRoomImpl.saveWeather(position,weather)
    }

    fun getWeatherFromRemoteServer(lat:Double,lon:Double){
        liveData.postValue(AppStateWeather.Loading(0))
        repositoryRemoteImpl.getWeatherFromServer(lat,lon,callback)
    }

    fun convertDTOtoModel(weatherDTO: WeatherDTO):Weather{
        return Weather(getDefaultCity(),weatherDTO.fact.temp.toInt(),weatherDTO.fact.feelsLike.toInt(),weatherDTO.fact.icon)
    }

    private val callback = object :Callback<WeatherDTO>{
        override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
            liveData.postValue(AppStateWeather.Error(R.string.errorOnServer,0))
        }

        override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
            if (response.isSuccessful){
                response.body()?.let {
                    liveData.postValue(AppStateWeather.Success(convertDTOtoModel(it),it.fact.condition))
                }
            } else{
                liveData.postValue(AppStateWeather.Error(R.string.codeError,response.code()))
            }
        }



    }
}