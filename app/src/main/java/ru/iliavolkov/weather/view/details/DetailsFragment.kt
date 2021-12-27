package ru.iliavolkov.weather.view.details

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.iliavolkov.weather.databinding.FragmentDetailsBinding
import ru.iliavolkov.weather.databinding.FragmentMainBinding
import ru.iliavolkov.weather.model.Weather
import ru.iliavolkov.weather.view.main.MainFragmentAdapter
import ru.iliavolkov.weather.viewmodel.AppState
import ru.iliavolkov.weather.viewmodel.MainViewModel
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val weather = arguments?.getParcelable<Weather>(BUNDLE_KEY)
        if (weather!=null){
            binding.cityName.text = weather.city.nameCity
            binding.cityCoordinates.text = "${weather.city.lat} ${weather.city.lon}"
            binding.temperatureValue.text = "${weather.temperature}"
            binding.feelsLikeValue.text = "${weather.feelsLike}"
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle):DetailsFragment{
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment

        }
    }
}