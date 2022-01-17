package ru.iliavolkov.weather.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.iliavolkov.weather.databinding.FragmentDetailsBinding
import ru.iliavolkov.weather.model.Weather
import ru.iliavolkov.weather.model.WeatherDTO
import ru.iliavolkov.weather.utils.WeatherLoader

const val BUNDLE_KEY ="BUNDLE_KEY"
class DetailsFragment : Fragment(), WeatherLoader.OnWeatherLoader {

    private var _binding: FragmentDetailsBinding? = null
    private val binding:FragmentDetailsBinding
    get(){
        return _binding!!
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    private val weatherLoader = WeatherLoader(this)
    lateinit var localWeather:Weather
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            it.getParcelable<Weather>(BUNDLE_KEY)?.let { weather:Weather ->
                localWeather = weather

                weatherLoader.loadWeather(weather.city.lat,weather.city.lon)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle) = DetailsFragment().apply{arguments = bundle}
    }

    override fun onLoaded(weatherDTO: WeatherDTO?) {
        weatherDTO?.let {
            with(binding){
                cityName.text = localWeather.city.nameCity
                cityCoordinates.text = "${localWeather.city.lat} ${localWeather.city.lon}"
                temperatureValue.text = "${weatherDTO.fact.temp}"
                feelsLikeValue.text = "${weatherDTO.fact.feelsLike}"
            }
        }

    }

    override fun onFailed() {
        TODO("Not yet implemented")
    }
}