package ru.iliavolkov.weather.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import ru.iliavolkov.weather.R
import ru.iliavolkov.weather.databinding.ActivityMainBinding
import ru.iliavolkov.weather.view.contacts.ContactsFragment
import ru.iliavolkov.weather.view.history.HistoryFragment
import ru.iliavolkov.weather.view.main.MainFragment

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    //private val receiver = MyBroadcastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null){
            supportFragmentManager.beginTransaction().replace(R.id.container, MainFragment.newInstance()).commit()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_screen_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return return when (item.itemId) {
            R.id.menu_history -> {
                supportFragmentManager.beginTransaction()
                        .add(R.id.container, HistoryFragment.newInstance()).addToBackStack("").commit()
                true
            }
            R.id.menu_content -> {
                supportFragmentManager.beginTransaction()
                    .add(R.id.container, ContactsFragment.newInstance()).addToBackStack("").commit()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        //unregisterReceiver(receiver)
    }
}