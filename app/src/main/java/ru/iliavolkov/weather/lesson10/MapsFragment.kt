package ru.iliavolkov.weather.lesson10

import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ru.iliavolkov.weather.R
import ru.iliavolkov.weather.databinding.FragmentGoogleMapMainBinding
import ru.iliavolkov.weather.viewmodel.AppStateWeather
import ru.iliavolkov.weather.viewmodel.DetailsViewModel

class MapsFragment : Fragment() {

    private var _binding: FragmentGoogleMapMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailsViewModel by lazy { ViewModelProvider(this).get(DetailsViewModel::class.java) }

    private val callback = OnMapReadyCallback { googleMap ->
        val moscow = LatLng(55.755826, 37.617299900000035)
        googleMap.addMarker(MarkerOptions().position(moscow).title("Moscow"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(moscow))
        viewModel.getWeatherFromRemoteServer(moscow.latitude,moscow.longitude)

        googleMap.setOnMapClickListener {
            googleMap.clear()
            Thread{
                val listAddress = Geocoder(requireContext()).getFromLocation(it.latitude,it.longitude,1)
                if (listAddress[0] != null){
                    requireActivity().runOnUiThread {
                        if (listAddress[0].locality!=null){
                            binding.cityName.text = listAddress[0].locality
                        } else if(listAddress[0].adminArea!=null){
                            binding.cityName.text = listAddress[0].adminArea
                        }
                        binding.cityName.setOnClickListener {
                            Toast.makeText(requireContext(),binding.cityName.text,Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }.start()
            val position = LatLng(it.latitude,it.longitude)
            viewModel.getWeatherFromRemoteServer(it.latitude,it.longitude)
            googleMap.addMarker(MarkerOptions().position(position))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(position))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGoogleMapMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun renderData(it: AppStateWeather?) {
        when(it){
            is AppStateWeather.Success ->{
                with(binding){
                    temperature.text = it.weatherData.temperature.toString()
                    feelsLike.text = it.weatherData.feelsLike.toString()
                    iconWeather.loadIconSvg("https://yastatic.net/weather/i/icons/funky/dark/${it.weatherData.icon}.svg")
                }
            }
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

    companion object {
        @JvmStatic
        fun newInstance() = MapsFragment()
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}