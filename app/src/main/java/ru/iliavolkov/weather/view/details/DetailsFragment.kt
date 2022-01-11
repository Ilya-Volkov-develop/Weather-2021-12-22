package ru.iliavolkov.weather.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.iliavolkov.weather.databinding.FragmentDetailsBinding
import ru.iliavolkov.weather.model.Weather

const val BUNDLE_KEY ="BUNDLE_KEY"
class DetailsFragment : Fragment() {

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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val weather = arguments?.getParcelable<Weather>(BUNDLE_KEY)
        arguments?.let {
            it.getParcelable<Weather>(BUNDLE_KEY)?.let { weather:Weather ->
                with(binding){
                    cityName.text = weather.city.nameCity
                    cityCoordinates.text = "${weather.city.lat} ${weather.city.lon}"
                    temperatureValue.text = "${weather.temperature}"
                    feelsLikeValue.text = "${weather.feelsLike}"
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle) = DetailsFragment().apply{arguments = bundle}
    }
}