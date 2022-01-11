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
import com.google.android.material.snackbar.Snackbar
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

    private val adapter:MainFragmentAdapter by lazy {
        MainFragmentAdapter(this)
    }
    private var isRussian = false

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMainBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        viewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })
        viewModel.getWeatherFromLocalSourceRus()
    }

    private fun initView() {
        with(binding){
            mainFragmentRecyclerView.adapter = adapter
            mainFragmentFAB.setOnClickListener {
                restart()
                if (!isRussian) mainFragmentFAB.setImageResource(R.drawable.ic_earth)
                else mainFragmentFAB.setImageResource(R.drawable.ic_russia)
                isRussian = !isRussian
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun renderData(appState: AppState) {
        with(binding){
            when(appState){
                is AppState.Error -> {
                    Toast.makeText(requireContext(),appState.error, Toast.LENGTH_SHORT).show()
                    restart()
                }
                is AppState.Loading -> {
                    mainFragmentLoadingLayout.visibility = View.VISIBLE
                }
                is AppState.Success -> {
                    mainFragmentLoadingLayout.visibility = View.GONE
                    adapter.setWeather(appState.weatherData)
                    binding.root.showSnackBarWithoutActivity("Успешно",Snackbar.LENGTH_SHORT)
                }
            }
        }

    }

    fun View.showSnackBarWithoutActivity(text:String,length:Int){
        Snackbar.make(this,text,length).show()
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }

    fun restart(){
        with(viewModel){
            if (isRussian) getWeatherFromLocalSourceRus()
            else getWeatherFromLocalSourceWorld()
        }

    }

    override fun onItemClick(weather: Weather) {
        activity?.run{
            supportFragmentManager.beginTransaction()
                    .add(R.id.container,
                            DetailsFragment.newInstance(
                                    Bundle().apply {
                                        putParcelable(BUNDLE_KEY,weather)
                                    }
                            ))
                    .addToBackStack("").commit()
        }
    }
}