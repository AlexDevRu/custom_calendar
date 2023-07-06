package com.vironit.learning_android_custom_calendar_kulakov.calendar_v2

import android.app.Application
import android.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vironit.learning_android_custom_calendar_kulakov.Utils
import com.vironit.learning_android_custom_calendar_kulakov.events.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class CalendarViewModel(private val app: Application): AndroidViewModel(app) {

    private val _events = MutableLiveData<List<Event>>()
    val events : LiveData<List<Event>> = _events

    fun getAllEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            _events.postValue(Utils.getAllEvents(app.contentResolver))
            //Utils.getAllEvents(app.contentResolver)
            /*val event = Event(
                id = 0L,
                title = "TEST",
                calendar = Calendar.getInstance(),
                color = Color.BLUE
            )
            Utils.insertNewEvent(app.contentResolver, event)*/
        }
    }



}