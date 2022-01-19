package ru.iliavolkov.weather.view.details

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import ru.iliavolkov.weather.R
import ru.iliavolkov.weather.databinding.FragmentDetailsBinding
import ru.iliavolkov.weather.model.Weather
import ru.iliavolkov.weather.model.WeatherDTO
import ru.iliavolkov.weather.utils.*

class DetailsFragment  :Fragment(), WeatherLoader.OnWeatherLoader {

    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get() {
            return _binding!!
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val receiver:BroadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let{
                it.getParcelableExtra<WeatherDTO>(BUNDLE_KEY_WEATHER)?.let{ weather->
                    setWeatherData(weather)
                }
            }
        }
    }

    lateinit var localWeather: Weather
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            it.getParcelable<Weather>(BUNDLE_KEY_MAIN_FRAGMENT_IN_DETAILS_FRAGMENT)?.let { weather ->
                localWeather = weather
                requireActivity().startService(Intent(requireActivity(),DetailsService::class.java).apply {
                    putExtra(KEY_LAT,localWeather.city.lat)
                    putExtra(KEY_LON,localWeather.city.lon)
                })
            }
        }
        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(receiver, IntentFilter(BROADCAST_ACTION))
    }

    //Устанавливаем данные в фрагмент
    private fun setWeatherData(weatherDTO: WeatherDTO) {
        weatherDTO?.let {
            with(binding){
                temperatureLabel.visibility = View.VISIBLE
                feelsLikeLabel.visibility = View.VISIBLE
                cityName.text = localWeather.city.nameCity
                cityCoordinates.text = "${weatherDTO.info.lat} ${weatherDTO.info.lon}"
                temperatureValue.text = "${weatherDTO.fact.temp}"
                feelsLikeValue.text = "${weatherDTO.fact.feelsLike}"
            }
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    companion object {
        fun newInstance(bundle: Bundle)= DetailsFragment().apply { arguments = bundle }
    }

    override fun onLoaded(weatherDTO: WeatherDTO?) {
        weatherDTO?.let {
            with(binding){
                temperatureLabel.visibility = View.VISIBLE
                feelsLikeLabel.visibility = View.VISIBLE
                cityName.text = localWeather.city.nameCity
                cityCoordinates.text = "${weatherDTO.info.lat} ${weatherDTO.info.lon}"
                temperatureValue.text = "${weatherDTO.fact.temp}"
                feelsLikeValue.text = "${weatherDTO.fact.feelsLike}"
            }
        }
    }
    override fun onFailed() {
        val dialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        val inflater: LayoutInflater? = LayoutInflater.from(requireContext())
        val exitView: View = inflater!!.inflate(R.layout.dialog_error, null)
        dialog.setView(exitView)
        val dialog1: android.app.Dialog? = dialog.create()
        val ok: Button = exitView.findViewById(R.id.ok)
        dialog1?.setCancelable(false)
        ok.setOnClickListener {
            dialog1?.dismiss()
            requireActivity().onBackPressed()
        }
        dialog1?.show()
    }

}