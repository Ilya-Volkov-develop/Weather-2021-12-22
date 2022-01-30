package ru.iliavolkov.weather.view.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.iliavolkov.weather.databinding.FragmentHistoryBinding
import ru.iliavolkov.weather.model.Weather
import ru.iliavolkov.weather.viewmodel.AppStateBD
import ru.iliavolkov.weather.viewmodel.HistoryViewModel

class HistoryFragment : Fragment(),OnItemClickListener {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val adapter: CitiesHistoryAdapter by lazy {
        CitiesHistoryAdapter(this)
    }

    private val viewModel: HistoryViewModel by lazy {
        ViewModelProvider(this).get(HistoryViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHistoryBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.historyFragmentRecyclerview.adapter = adapter
        viewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })
        viewModel.getAllHistory()
    }

    private fun renderData(appStateBD: AppStateBD) {
        when(appStateBD){
            is AppStateBD.Error ->{}
            is AppStateBD.Success ->{
                adapter.setWeather(appStateBD.weatherInfoHistoryData)
            }
            else -> {}
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() = HistoryFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemClick(weather: Weather) {
        viewModel.delete(weather)
        Toast.makeText(context,"delete",Toast.LENGTH_SHORT).show()
    }
}