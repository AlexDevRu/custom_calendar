package com.vironit.learning_android_custom_calendar_kulakov.calendar_v2

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.vironit.learning_android_custom_calendar_kulakov.Utils
import com.vironit.learning_android_custom_calendar_kulakov.databinding.ActivityFullScreenBinding

class FullScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFullScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val flags = (WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                or WindowManager.LayoutParams.FLAG_FULLSCREEN)

        window.addFlags(flags)

        val eventId = intent.getLongExtra("eventId", 0L)
        Log.d(javaClass.simpleName, "onReceive: eventId=$eventId")
        val event = Utils.getEventById(contentResolver, eventId)
        Log.d(javaClass.simpleName, "onReceive: event=$event")
        binding.tvTitle.text = event?.title
    }

}