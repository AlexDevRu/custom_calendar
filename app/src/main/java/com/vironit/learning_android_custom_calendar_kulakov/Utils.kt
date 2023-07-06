package com.vironit.learning_android_custom_calendar_kulakov

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.graphics.Color
import android.provider.CalendarContract
import android.provider.CalendarContract.Events
import android.util.Log
import androidx.core.database.getIntOrNull
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import com.vironit.learning_android_custom_calendar_kulakov.events.Event
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.TimeZone


object Utils {

    fun getAllEvents(contentResolver: ContentResolver) : List<Event> {
        val events = mutableListOf<Event>()

        val uri = CalendarContract.Calendars.CONTENT_URI
        val calendarProjection = arrayOf(
            CalendarContract.Calendars._ID,
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
        )
        val cur = contentResolver.query(uri, calendarProjection, null, null, null)
        if (cur != null) {
            while (cur.moveToNext()) {
                val calId = cur.getLong(cur.getColumnIndexOrThrow(CalendarContract.Calendars._ID))
                val calendarName = cur.getString(cur.getColumnIndexOrThrow(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME))
                Log.e(javaClass.simpleName, "id=$calId calendarName=$calendarName")

                val projection1: Array<String> = arrayOf(
                    Events._ID,
                    Events.DTSTART,
                    Events.DESCRIPTION,
                    Events.TITLE,
                    Events.DISPLAY_COLOR,
                    Events.DELETED,
                )

                val uri1 = Events.CONTENT_URI
                val selection1 = "(${Events.CALENDAR_ID} = ?)"
                val selectionArgs1 = arrayOf(calId.toString())
                val cur1 = contentResolver.query(
                    uri1,
                    projection1,
                    selection1, selectionArgs1,
                    null,
                )
                if (cur1 != null) {
                    while(cur1.moveToNext()) {
                        val eventId = cur1.getLong(cur1.getColumnIndexOrThrow(Events._ID))
                        val title = cur1.getStringOrNull(cur1.getColumnIndexOrThrow(Events.TITLE))
                        val dtstart = cur1.getLongOrNull(cur1.getColumnIndexOrThrow(Events.DTSTART))
                        val displayColor = cur1.getIntOrNull(cur1.getColumnIndexOrThrow(Events.DISPLAY_COLOR))
                        val deleted = cur1.getIntOrNull(cur1.getColumnIndexOrThrow(Events.DELETED))
                        Log.d(javaClass.simpleName, "eventId = $eventId, title=$title, dtstart=$dtstart, displayColor=$displayColor")
                        if (deleted != 1)
                            events.add(
                                Event(
                                    id = eventId,
                                    title = title.orEmpty(),
                                    calendar = Calendar.getInstance().also { it.timeInMillis = dtstart ?: 0L },
                                    color = displayColor ?: Color.BLACK
                                )
                            )
                    }
                    cur1.close()
                }
            }
            cur.close()
        }

        return events.sortedBy { it.calendar.timeInMillis }
    }

    fun insertNewEvent(contentResolver: ContentResolver, event: Event) {
        val calId: Long = 1L
        if (calId == -1L) {
            // no calendar account; react meaningfully
            return
        }
        GregorianCalendar()
        val cal = GregorianCalendar(
            event.calendar.get(Calendar.YEAR),
            event.calendar.get(Calendar.MONTH),
            event.calendar.get(Calendar.DAY_OF_MONTH),
            event.calendar.get(Calendar.HOUR),
            event.calendar.get(Calendar.MINUTE),
            event.calendar.get(Calendar.SECOND),
        )
        cal.timeZone = TimeZone.getTimeZone("UTC")
        val values = ContentValues()
        values.put(Events.DTSTART, event.calendar.timeInMillis)
        values.put(Events.DTEND, event.calendar.timeInMillis)
        values.put(Events.TITLE, event.title)
        values.put(Events.CALENDAR_ID, calId)
        values.put(Events.EVENT_COLOR, event.color)
        values.put(Events.EVENT_TIMEZONE, cal.timeZone.displayName)

        values.put(Events.ACCESS_LEVEL, Events.ACCESS_PRIVATE)
        values.put(
            Events.SELF_ATTENDEE_STATUS,
            Events.STATUS_CONFIRMED
        )
        values.put(Events.ALL_DAY, 0)
        values.put(Events.AVAILABILITY, Events.AVAILABILITY_BUSY)
        val uri = contentResolver.insert(Events.CONTENT_URI, values)
        val eventId = uri?.lastPathSegment
        Log.e(javaClass.simpleName ,"EVENT ID $eventId")
    }

    fun removeEvent(contentResolver: ContentResolver, eventId: Long) {
        val uri = ContentUris.withAppendedId(Events.CONTENT_URI, eventId)
        contentResolver.delete(uri, null, null)
    }
}