package ru.iliavolkov.weather.lesson6

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Vibrator
import android.widget.Toast

class MyBroadcastReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "Состояние сети изменино", Toast.LENGTH_SHORT).show()
            val vibe: Vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibe.vibrate(300)

    }
}