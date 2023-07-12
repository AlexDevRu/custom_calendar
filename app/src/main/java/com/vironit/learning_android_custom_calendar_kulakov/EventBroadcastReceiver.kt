package com.vironit.learning_android_custom_calendar_kulakov

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.vironit.learning_android_custom_calendar_kulakov.calendar_v2.FullScreenActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class EventBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val eventId = intent.getLongExtra("eventId", 0L)
        Log.d(javaClass.simpleName, "onReceive: eventId=$eventId")

        val fullScreenIntent = Intent(context, FullScreenActivity::class.java)
            .putExtra("eventId", eventId)
        val fullScreenPendingIntent = PendingIntent.getActivity(context, 0,
            fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        CoroutineScope(Job() + Dispatchers.IO).launch {
            val event = Utils.getEventById(context.contentResolver, eventId)
            Log.d(javaClass.simpleName, "onReceive: event=$event")

            if (event != null) {
                val notification = NotificationCompat.Builder(context, "Calendar")
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle(event.title)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_CALL)
                    .setFullScreenIntent(fullScreenPendingIntent, true)
                    .build()

                (context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager).notify(1, notification)
            }
        }
    }

}