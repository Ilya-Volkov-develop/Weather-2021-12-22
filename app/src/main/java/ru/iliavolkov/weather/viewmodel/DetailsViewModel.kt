package ru.iliavolkov.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.iliavolkov.weather.model.Weather
import ru.iliavolkov.weather.model.WeatherDTO
import ru.iliavolkov.weather.model.getDefaultCity
import ru.iliavolkov.weather.repository.RepositoryImpl

class DetailsViewModel(private val liveData: MutableLiveData<AppStateWeather> = MutableLiveData()): ViewModel() {

    private val repositoryImpl: RepositoryImpl by lazy {
        RepositoryImpl()
    }

    fun getLiveData() = liveData

    fun getWeatherFromRemoteServer(lat:Double,lon:Double){
        liveData.postValue(AppStateWeather.Loading(0))
        repositoryImpl.getWeatherFromServer(lat,lon,callback)
    }

    fun convertDTOtoModel(weatherDTO: WeatherDTO):Weather{
        return Weather(getDefaultCity(),weatherDTO.fact.temp.toInt(),weatherDTO.fact.feelsLike.toInt())
    }

    private val callback = object :Callback<WeatherDTO>{
        override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
            TODO("Not yet implemented")
        }

        override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
            if (response.isSuccessful){
                response.body()?.let {
                    liveData.postValue(AppStateWeather.Success(convertDTOtoModel(it)))
                }
            } else{
                liveData.postValue(AppStateWeather.Error(response.code().toString()))
            }
        }



    }
}