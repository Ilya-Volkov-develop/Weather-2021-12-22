package ru.iliavolkov.weather.view

import android.content.IntentFilter
import android.net.ConnectivityManager.CONNECTIVITY_ACTION
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.iliavolkov.weather.R
import ru.iliavolkov.weather.databinding.ActivityMainBinding
import ru.iliavolkov.weather.lesson6.MyBroadcastReceiver
import ru.iliavolkov.weather.view.main.MainFragment

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null){
            supportFragmentManager.beginTransaction().replace(R.id.container, MainFragment.newInstance()).commit()
        }

        registerReceiver(MyBroadcastReceiver(), IntentFilter(CONNECTIVITY_ACTION))
    }
}