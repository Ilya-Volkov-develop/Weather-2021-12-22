package ru.iliavolkov.weather.lesson11

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.google.firebase.messaging.FirebaseMessagingService
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.RemoteMessage
import ru.iliavolkov.weather.R

class MyFCMService : FirebaseMessagingService() {
    override fun onNewToken(s: String) {
        super.onNewToken(s)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val data = message.data.toMap()
        if(data.isNotEmpty()){
            val title =data[KEY_TITLE]
            val message =data[KEY_MESSAGE]
            if(!title.isNullOrBlank()&&!message.isNullOrBlank())
                pushNotification(title,message)
        }
    }


    companion object {
        private const val NOTIFICATION_ID_1 = 1
        private const val CHANNEL_ID_1 = "channel_id_1"
        private const val KEY_TITLE = "myTitle"
        private const val KEY_MESSAGE = "myMessage"
    }
    private fun pushNotification(title:String,message:String) {
        val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = NotificationCompat.Builder(this,CHANNEL_ID_1).apply {
            setSmallIcon(R.drawable.term)
            setContentTitle(title)
            setContentText(message)
            priority = NotificationCompat.PRIORITY_MAX
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channelName = "Name $CHANNEL_ID_1"
            val channelDescription = "Description for $CHANNEL_ID_1"
            val channelPriority = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(CHANNEL_ID_1,channelName,channelPriority).apply {
                description = channelDescription
            }
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(NOTIFICATION_ID_1,notificationBuilder.build())
    }
}