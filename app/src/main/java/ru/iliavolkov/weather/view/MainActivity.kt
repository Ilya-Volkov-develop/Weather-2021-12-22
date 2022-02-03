package ru.iliavolkov.weather.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import ru.iliavolkov.weather.R
import ru.iliavolkov.weather.databinding.ActivityMainBinding
import ru.iliavolkov.weather.lesson10.MapsFragment
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
                val fragmentTag = supportFragmentManager.findFragmentByTag("HistoryFragment")
                if(fragmentTag==null) {
                    supportFragmentManager.beginTransaction()
                        .add(R.id.container, HistoryFragment.newInstance(),"HistoryFragment").addToBackStack("").commit()
                }
                true
            }
            R.id.menu_contacts -> {
                val fragmentTag = supportFragmentManager.findFragmentByTag("ContactsFragment")
                if(fragmentTag==null) {
                    supportFragmentManager.beginTransaction()
                        .add(R.id.container, ContactsFragment.newInstance(),"ContactsFragment").addToBackStack("").commit()
                }
                true
            }
            R.id.menu_map -> {
                val fragmentTag = supportFragmentManager.findFragmentByTag("MapsFragment")
                if(fragmentTag==null){
                    supportFragmentManager.beginTransaction()
                        .add(R.id.container, MapsFragment.newInstance(),"MapsFragment").addToBackStack("").commit()
                }
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