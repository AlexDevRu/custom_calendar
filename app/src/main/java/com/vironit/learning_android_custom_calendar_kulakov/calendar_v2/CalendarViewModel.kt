package com.vironit.learning_android_custom_calendar_kulakov.calendar_v2

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.CalendarContract
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vironit.learning_android_custom_calendar_kulakov.EventBroadcastReceiver
import com.vironit.learning_android_custom_calendar_kulakov.Utils
import com.vironit.learning_android_custom_calendar_kulakov.events.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class CalendarViewModel(private val app: Application): AndroidViewModel(app) {

    private val alarmManager = app.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private val _events = MutableLiveData<List<Event>>()
    val events : LiveData<List<Event>> = _events

    private val observer = object : ContentObserver(Handler(Looper.getMainLooper())) {
        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            getAllEvents()
        }
    }

    init {
        app.contentResolver.registerContentObserver(CalendarContract.Events.CONTENT_URI, false, observer)
    }

    fun getAllEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            _events.postValue(Utils.getAllEvents(app.contentResolver))
        }
    }

    fun insertEvent(date: Long, title: String, color: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val event = Event(
                id = System.currentTimeMillis(),
                calendar = Calendar.getInstance().also { it.timeInMillis = date },
                title = title,
                color = color
            )
            val eventId = Utils.insertNewEvent(app.contentResolver, event)
            val intent = Intent(app, EventBroadcastReceiver::class.java)
                .putExtra("eventId", eventId)
            val pendingIntent = PendingIntent.getBroadcast(app, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, date, pendingIntent)
            }
        }
    }

    fun removeEvent(eventId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            Utils.removeEvent(app.contentResolver, eventId)
        }
    }

    override fun onCleared() {
        super.onCleared()
        app.contentResolver.unregisterContentObserver(observer)
    }


}