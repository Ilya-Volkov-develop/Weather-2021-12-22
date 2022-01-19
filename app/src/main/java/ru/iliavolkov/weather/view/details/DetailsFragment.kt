package ru.iliavolkov.weather.view.details

import android.app.AlertDialog
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
import ru.iliavolkov.weather.utils.BROADCAST_ACTION
import ru.iliavolkov.weather.utils.BUNDLE_KEY_MAIN_FRAGMENT_IN_DETAILS_FRAGMENT
import ru.iliavolkov.weather.utils.KEY_LAT
import ru.iliavolkov.weather.utils.KEY_LON

class DetailsFragment  :Fragment(),DetailsReceiver.ReceiverTotal {

    private val receiver = DetailsReceiver(this)
    lateinit var localWeather: Weather
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

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
        with(binding){
            temperatureLabel.visibility = View.VISIBLE
            feelsLikeLabel.visibility = View.VISIBLE
            cityName.text = localWeather.city.nameCity
            cityCoordinates.text = "${weatherDTO.info.lat} ${weatherDTO.info.lon}"
            temperatureValue.text = "${weatherDTO.fact.temp}"
            feelsLikeValue.text = "${weatherDTO.fact.feelsLike}"
        }
    }

    //при ошибке всплывает диалоговое окно
     fun loadingFailed() {
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

    override fun success(weatherDTO: WeatherDTO) {
        setWeatherData(weatherDTO)
    }

    override fun error() {
        loadingFailed()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        context?.let {
            LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(receiver)
        }
    }
    companion object {
        fun newInstance(bundle: Bundle)= DetailsFragment().apply { arguments = bundle }
    }
}