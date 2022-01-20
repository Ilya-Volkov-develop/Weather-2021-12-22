package ru.iliavolkov.weather.view.details

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.iliavolkov.weather.R
import ru.iliavolkov.weather.databinding.FragmentDetailsBinding
import ru.iliavolkov.weather.model.Weather
import ru.iliavolkov.weather.utils.BUNDLE_KEY_MAIN_FRAGMENT_IN_DETAILS_FRAGMENT
import ru.iliavolkov.weather.viewmodel.AppState
import ru.iliavolkov.weather.viewmodel.DetailsViewModel

class DetailsFragment  :Fragment() {

    lateinit var localWeather: Weather
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(viewLifecycleOwner, {
            renderData(it)
        })
        arguments?.let {
            it.getParcelable<Weather>(BUNDLE_KEY_MAIN_FRAGMENT_IN_DETAILS_FRAGMENT)?.let { weather ->
                localWeather = weather
                viewModel.getWeatherFromRemoteServer(localWeather.city.lat.toString(),localWeather.city.lon.toString())
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun renderData(appState: AppState) {
        with(binding){
            when(appState){
                is AppState.Error -> {
                    loadingFailed()
                }
                is AppState.Loading -> {
                    loadingLayout.visibility = View.VISIBLE
                }
                is AppState.Success -> {
                    loadingLayout.visibility = View.GONE
                    val weather = appState.weatherData[0]
                    setWeatherData(weather)
                }
            }
        }

    }

    //Устанавливаем данные в фрагмент
    @SuppressLint("SetTextI18n")
    private fun setWeatherData(weather: Weather) {
        with(binding){
            temperatureLabel.visibility = View.VISIBLE
            feelsLikeLabel.visibility = View.VISIBLE
            cityName.text = localWeather.city.nameCity
            cityCoordinates.text = "${weather.city.lat} ${weather.city.lon}"
            temperatureValue.text = "${weather.temperature}"
            feelsLikeValue.text = "${weather.feelsLike}"
        }
    }

    //при ошибке всплывает диалоговое окно
    private fun loadingFailed() {
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    companion object {
        fun newInstance(bundle: Bundle)= DetailsFragment().apply { arguments = bundle }
    }
}