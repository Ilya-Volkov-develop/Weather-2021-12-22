package ru.iliavolkov.weather.view.main

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.iliavolkov.weather.R
import ru.iliavolkov.weather.databinding.FragmentMainBinding
import ru.iliavolkov.weather.model.City
import ru.iliavolkov.weather.utils.BUNDLE_KEY_MAIN_FRAGMENT_IN_DETAILS_FRAGMENT
import ru.iliavolkov.weather.view.details.DetailsFragment
import ru.iliavolkov.weather.viewmodel.AppStateCity
import ru.iliavolkov.weather.viewmodel.MainViewModel

class MainFragment : Fragment(),OnItemClickListener {


    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val adapter:MainFragmentAdapter by lazy {
        MainFragmentAdapter(this)
    }

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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
        }
    }

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
                    //binding.root.showSnackBarWithoutActivity("Успешно",Snackbar.LENGTH_SHORT)
                }
            }
        }

    }

//    private fun View.showSnackBarWithoutActivity(text:String, length:Int){
//        Snackbar.make(this,text,length).show()
//    }



    override fun onItemClick(city: City) {
        activity?.run{
            supportFragmentManager.beginTransaction()
                    .add(R.id.container,
                            DetailsFragment.newInstance(Bundle().apply {
                                        putParcelable(BUNDLE_KEY_MAIN_FRAGMENT_IN_DETAILS_FRAGMENT,city)
                                    }
                            ))
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