package ru.iliavolkov.weather.view.details

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import ru.iliavolkov.weather.R
import ru.iliavolkov.weather.databinding.FragmentDetailsBinding
import ru.iliavolkov.weather.model.City
import ru.iliavolkov.weather.model.Weather
import ru.iliavolkov.weather.utils.BUNDLE_KEY_MAIN_FRAGMENT_IN_DETAILS_FRAGMENT
import ru.iliavolkov.weather.viewmodel.AppStateWeather
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
            it.getParcelable<City>(BUNDLE_KEY_MAIN_FRAGMENT_IN_DETAILS_FRAGMENT)?.let { city ->
                localWeather = Weather(city,city.lat.toInt(),city.lon.toInt())
                viewModel.getWeatherFromRemoteServer(city.lat,city.lon)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun renderData(appStateWeather: AppStateWeather) {
        with(binding){
            when(appStateWeather){
                is AppStateWeather.Error -> {
                    loadingFailed(appStateWeather.error,appStateWeather.code)
                }
                is AppStateWeather.Loading -> {
                    loadingLayout.visibility = View.VISIBLE
                }
                is AppStateWeather.Success -> {
                    loadingLayout.visibility = View.GONE
                    val weather = appStateWeather.weatherData
                    val urlIcon = appStateWeather.icon
                    val condition = convertConditionEngToRus(appStateWeather.condition)
                    setWeatherData(weather,urlIcon,condition)
                }
            }
        }

    }

    private fun convertConditionEngToRus(condition: String): String {
        return when(condition){
            "clear" -> "ясно"
            "partly-cloudy" -> "малооблачно"
            "cloudy" -> "облачно с прояснениями"
            "overcast" -> "пасмурно"
            "drizzle" -> "морось"
            "light-rain" -> "небольшой дождь"
            "rain" -> "дождь"
            "moderate-rain" -> "умеренно сильный дождь"
            "heavy-rain" -> "сильный дождь"
            "continuous-heavy-rain" -> "длительный сильный дождь"
            "showers" -> "ливень"
            "wet-snow" -> "дождь со снегом"
            "light-snow" -> "небольшой снег"
            "snow" -> "снег"
            "snow-showers" -> "снегопад"
            "hail" -> "град"
            "thunderstorm" -> "гроза"
            "thunderstorm-with-rain" -> "дождь с грозой"
            "thunderstorm-with-hail" -> "гроза с градом"
            else -> ""
        }
    }

    //Устанавливаем данные в фрагмент
    @SuppressLint("SetTextI18n")
    private fun setWeatherData(weather: Weather, urlIcon: String, conditionText: String) {

        with(binding){
            iconWeather.setOnClickListener {
                viewModel.saveWeather(weather)
            }
            temperatureLabel.visibility = View.VISIBLE
            feelsLikeLabel.visibility = View.VISIBLE
            cityName.text = localWeather.city.nameCity
            cityCoordinates.text = "${weather.city.lat} ${weather.city.lon}"
            temperatureValue.text = "${weather.temperature}"
            feelsLikeValue.text = "${weather.feelsLike}"
            condition.text = conditionText
            iconWeather.loadIconSvg("https://yastatic.net/weather/i/icons/funky/dark/$urlIcon.svg")
        }
    }

    private fun ImageView.loadIconSvg(url:String){
        val imageLoader = ImageLoader.Builder(this.context)
                .componentRegistry{add(SvgDecoder(this@loadIconSvg.context))}
                .build()

        val request = ImageRequest.Builder(this.context)
                .data(url)
                .target(this)
                .build()
        imageLoader.enqueue(request)
    }

    //при ошибке всплывает диалоговое окно
    private fun loadingFailed(textId: Int, code: Int) {
        val dialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        val inflater: LayoutInflater? = LayoutInflater.from(requireContext())
        val exitView: View = inflater!!.inflate(R.layout.dialog_error, null)
        dialog.setView(exitView)
        val dialog1: Dialog? = dialog.create()
        val ok: Button = exitView.findViewById(R.id.ok)
        val codeTextView = exitView.findViewById<TextView>(R.id.textError)

        codeTextView.text = when(textId) {
            R.string.errorOnServer -> getString(R.string.errorOnServer)
            R.string.codeError -> getString(R.string.codeError) + " " + code
            else -> ""
        }
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