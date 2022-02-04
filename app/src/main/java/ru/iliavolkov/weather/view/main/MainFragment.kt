package ru.iliavolkov.weather.view.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.iliavolkov.weather.R
import ru.iliavolkov.weather.databinding.FragmentMainBinding
import ru.iliavolkov.weather.model.City
import ru.iliavolkov.weather.utils.*
import ru.iliavolkov.weather.view.details.DetailsFragment
import ru.iliavolkov.weather.viewmodel.AppStateCity
import ru.iliavolkov.weather.viewmodel.MainViewModel

class MainFragment : Fragment(),OnItemClickListener {


    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val adapter:MainFragmentAdapter by lazy { MainFragmentAdapter(this) }
    private val viewModel: MainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMainBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        viewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })

    }

    private fun initView() {
        var isRussian = requireActivity().getPreferences(Activity.MODE_PRIVATE).getBoolean("isRussian",true)
        initLocation(isRussian)
        with(binding){
            mainFragmentRecyclerView.adapter = adapter
            mainFragmentFAB.setOnClickListener {
                isRussian = !isRussian
                initLocation(isRussian)
            }
            mainFragmentFABLocation.setOnClickListener {
                checkPermission()
            }
        }
    }
    //region location
    private fun checkPermission() {
        context?.let {
            when {
                ContextCompat.checkSelfPermission(
                        it,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED -> {
                    getLocation()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    showDialogRatio()
                }
                else -> {
                    myRequestPermission()
                }
            }
        }
    }


    private fun getAddress(location: Location){
        Thread{
            val listAddress = Geocoder(requireContext()).getFromLocation(location.latitude,location.longitude,1)
            if (listAddress != null)
                requireActivity().runOnUiThread{
                    showDialogAddress(listAddress[0].getAddressLine(0),location)
                }
        }.start()
    }
    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
                getAddress(location)
        }
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }


    private fun getLocation(){
        activity?.let {
            if(ContextCompat.checkSelfPermission(
                            it,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    )==PackageManager.PERMISSION_GRANTED){
                val locationManager = it.getSystemService(LOCATION_SERVICE) as LocationManager
                if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                    val providerGPS = locationManager.getProvider(LocationManager.NETWORK_PROVIDER)
                    providerGPS?.let {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                                REFRESH_PERIOD,
                                MIN_DISTANCE,
                                locationListener
                        )
                    }
                }else{
                    val lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    lastLocation?.let{
                        getAddress(it)
                    }
                }
            }
        }
    }

    private fun showDialogAddress(address:String,location: Location){
        AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.dialog_address_title))
                .setMessage(address)
                .setPositiveButton(getString(R.string.dialog_address_get_weather)) { _, _ ->
                    requireActivity().supportFragmentManager.beginTransaction()
                                .add(R.id.container,
                                        DetailsFragment.newInstance(Bundle().apply {
                                            putParcelable(BUNDLE_KEY_MAIN_FRAGMENT_IN_DETAILS_FRAGMENT,
                                                    City(address,location.latitude,location.longitude))
                                        }))
                                .addToBackStack("").commit()
                    }
                .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
    }


    private fun myRequestPermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE) {

            when {
                (grantResults[0] == PackageManager.PERMISSION_GRANTED) -> {
                    getLocation()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    showDialogRatio()
                }
                else -> {
                    Log.d("", "КОНЕЦ")
                }
            }
        }
    }

    private fun showDialogRatio() {
        AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.dialog_rationale_title))
                .setMessage(getString(R.string.dialog_message_no_gps))
                .setPositiveButton(getString(R.string.dialog_rationale_give_access)) { _, _ ->
                    myRequestPermission()
                }
                .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()

    }
    //endregion

    private fun initLocation(isRussian: Boolean) {
        with(viewModel){
            if (isRussian) getWeatherFromLocalSourceRus()
            else getWeatherFromLocalSourceWorld()
        }
        if (isRussian) {
            viewModel.getWeatherFromLocalSourceRus()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_russia)
            requireActivity().getPreferences(Activity.MODE_PRIVATE).edit().putBoolean("isRussian", true).apply()
        } else {
            viewModel.getWeatherFromLocalSourceWorld()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_earth)
            requireActivity().getPreferences(Activity.MODE_PRIVATE).edit().putBoolean("isRussian", false).apply()
        }
    }



    @SuppressLint("SetTextI18n")
    private fun renderData(appStateCity: AppStateCity) {
        with(binding){
            when(appStateCity){
                is AppStateCity.Error -> {
                    Toast.makeText(requireContext(),appStateCity.error, Toast.LENGTH_SHORT).show()
                    initView()
                }
                is AppStateCity.Loading -> {
                    mainFragmentLoadingLayout.visibility = View.VISIBLE
                }
                is AppStateCity.Success -> {
                    mainFragmentLoadingLayout.visibility = View.GONE
                    adapter.setWeather(appStateCity.cityData)
                }
            }
        }

    }
    override fun onItemClick(city: City,position:Int) {
        activity?.run{
            supportFragmentManager.beginTransaction()
                    .add(R.id.container,
                            DetailsFragment.newInstance(Bundle().apply {
                                putParcelable(BUNDLE_KEY_MAIN_FRAGMENT_IN_DETAILS_FRAGMENT,city)
                                putInt(BUNDLE_KEY_MAIN_FRAGMENT_IN_DETAILS_FRAGMENT_POSITION,position)
                            }))
                    .addToBackStack("").commit()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}