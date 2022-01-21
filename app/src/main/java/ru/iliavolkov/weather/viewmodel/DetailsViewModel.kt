package ru.iliavolkov.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import ru.iliavolkov.weather.model.Weather
import ru.iliavolkov.weather.model.WeatherDTO
import ru.iliavolkov.weather.model.getDefaultCity
import ru.iliavolkov.weather.repository.RepositoryImpl
import ru.iliavolkov.weather.utils.YANDEX_API_URL
import ru.iliavolkov.weather.utils.YANDEX_API_URL_END_POINT
import java.io.IOException

class DetailsViewModel(private val liveData: MutableLiveData<AppStateWeather> = MutableLiveData()): ViewModel() {

    private val repositoryImpl: RepositoryImpl by lazy {
        RepositoryImpl()
    }

    fun getLiveData() = liveData

    fun getWeatherFromRemoteServer(lat:String,lon:String){
        liveData.postValue(AppStateWeather.Loading(0))
        repositoryImpl.getWeatherFromServer(YANDEX_API_URL + YANDEX_API_URL_END_POINT + "?lat=${lat}&lon=${lon}",callback)
    }

    fun convertDTOtoModel(weatherDTO: WeatherDTO):Weather{
        return Weather(getDefaultCity(),weatherDTO.fact.temp.toInt(),weatherDTO.fact.feelsLike.toInt())
    }

    private val callback = object :Callback{
        override fun onFailure(call: Call, e: IOException) {
            TODO("Not yet implemented")
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful){
                response.body()?.let {
                    val jsonString = it.toString()
                    liveData.postValue(AppStateWeather.Success(convertDTOtoModel(Gson().fromJson(jsonString,WeatherDTO::class.java))))
                }
            } else{
                liveData.postValue(AppStateWeather.Error("forbidden"))
            }
        }

    }
}