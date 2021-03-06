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
import ru.iliavolkov.weather.utils.BUNDLE_KEY_MAIN_FRAGMENT_IN_DETAILS_FRAGMENT_POSITION
import ru.iliavolkov.weather.viewmodel.AppStateWeather
import ru.iliavolkov.weather.viewmodel.DetailsViewModel

class DetailsFragment  :Fragment() {

    lateinit var localWeather: Weather
    private var position: Int? = null
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailsViewModel by lazy { ViewModelProvider(this).get(DetailsViewModel::class.java) }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })
        arguments?.let {
            it.getParcelable<City>(BUNDLE_KEY_MAIN_FRAGMENT_IN_DETAILS_FRAGMENT)?.let { city ->
                localWeather = Weather(city,city.lat.toInt(),city.lon.toInt())
                viewModel.getWeatherFromRemoteServer(city.lat,city.lon)
            }
            position = it.getInt(BUNDLE_KEY_MAIN_FRAGMENT_IN_DETAILS_FRAGMENT_POSITION)
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
                    val condition = convertConditionEngToRus(appStateWeather.condition)
                    setWeatherData(weather,condition)
                }
            }
        }

    }

    private fun convertConditionEngToRus(condition: String): String {
        return when(condition){
            "clear" -> "????????"
            "partly-cloudy" -> "??????????????????????"
            "cloudy" -> "?????????????? ?? ????????????????????????"
            "overcast" -> "????????????????"
            "drizzle" -> "????????????"
            "light-rain" -> "?????????????????? ??????????"
            "rain" -> "??????????"
            "moderate-rain" -> "???????????????? ?????????????? ??????????"
            "heavy-rain" -> "?????????????? ??????????"
            "continuous-heavy-rain" -> "???????????????????? ?????????????? ??????????"
            "showers" -> "????????????"
            "wet-snow" -> "?????????? ???? ????????????"
            "light-snow" -> "?????????????????? ????????"
            "snow" -> "????????"
            "snow-showers" -> "????????????????"
            "hail" -> "????????"
            "thunderstorm" -> "??????????"
            "thunderstorm-with-rain" -> "?????????? ?? ????????????"
            "thunderstorm-with-hail" -> "?????????? ?? ????????????"
            else -> ""
        }
    }

    //?????????????????????????? ???????????? ?? ????????????????
    @SuppressLint("SetTextI18n")
    private fun setWeatherData(weather: Weather, conditionText: String) {
        position?.let {
            viewModel.saveWeather(it,Weather(City(localWeather.city.nameCity,0.0,0.0),weather.temperature,weather.feelsLike,weather.icon))
        }
        with(binding){
            temperatureLabel.visibility = View.VISIBLE
            feelsLikeLabel.visibility = View.VISIBLE
            cityName.text = localWeather.city.nameCity
            cityCoordinates.text = "${weather.city.lat} ${weather.city.lon}"
            temperatureValue.text = "${weather.temperature}"
            feelsLikeValue.text = "${weather.feelsLike}"
            condition.text = conditionText
            iconWeather.loadIconSvg("https://yastatic.net/weather/i/icons/funky/dark/${weather.icon}.svg")
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

    //?????? ???????????? ?????????????????? ???????????????????? ????????
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