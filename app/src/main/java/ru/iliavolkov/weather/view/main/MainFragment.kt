package ru.iliavolkov.weather.view.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.iliavolkov.weather.R
import ru.iliavolkov.weather.databinding.FragmentMainBinding
import ru.iliavolkov.weather.model.Weather
import ru.iliavolkov.weather.view.details.DetailsFragment
import ru.iliavolkov.weather.viewmodel.AppState
import ru.iliavolkov.weather.viewmodel.MainViewModel
const val BUNDLE_KEY ="BUNDLE_KEY"
class MainFragment : Fragment(),OnItemClickListener {

    private var _binding: FragmentMainBinding? = null
    private val binding:FragmentMainBinding
    get(){
        return _binding!!
    }

    private val adapter = MainFragmentAdapter(this)
    private var isRussian = false

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    lateinit var viewModel: MainViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer<AppState> { renderData(it) })
        binding.mainFragmentRecyclerView.adapter = adapter
        viewModel.getWeatherFromLocalSourceRus()
        binding.mainFragmentFAB.setOnClickListener{
            restart()
            if (!isRussian) binding.mainFragmentFAB.setImageResource(R.drawable.ic_earth)
            else binding.mainFragmentFAB.setImageResource(R.drawable.ic_russia)
            isRussian = !isRussian
        }
    }

    @SuppressLint("SetTextI18n")
    private fun renderData(appState: AppState) {
        when(appState){
            //is AppState.Error -> Toast.makeText(requireContext(),appState.error.message, Toast.LENGTH_SHORT).show()
            is AppState.Error -> {
                Toast.makeText(requireContext(),appState.error, Toast.LENGTH_SHORT).show()
                restart()
            }
            is AppState.Loading -> {
                binding.mainFragmentLoadingLayout.visibility = View.VISIBLE
            }
            is AppState.Success -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE
                adapter.setWeather(appState.weatherData)
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater,container,false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }

    fun restart(){
        if (isRussian) viewModel.getWeatherFromLocalSourceRus()
        else viewModel.getWeatherFromLocalSourceWorld()
    }

    override fun onItemClick(weather: Weather) {
        val bundle = Bundle()
        bundle.putParcelable(BUNDLE_KEY,weather)
        requireActivity().supportFragmentManager.beginTransaction().add(R.id.container,DetailsFragment.newInstance(bundle)).addToBackStack("").commit()
    }
}