package com.oleeb.calendarthai.event;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Build;

/**
 * Created by HackerOne on 12/12/2015.
 */
public class EventCalendarThai {
    public static long pushAppointmentsToCalender(Activity curActivity, String title,
                                                  String addInfo, String place, int status,
                                                  long startDate, boolean needReminder,
                                                  boolean needMailService) {
        /***************** Event: note(without alert) *******************/
        String eventUriString = "content://com.android.calendar/events";
        if (Build.VERSION.SDK_INT >= 8) {
            eventUriString = "content://com.android.calendar/events";
        } else {
            eventUriString = "content://calendar/events";
        }
        ContentValues eventValues = new ContentValues();

        eventValues.put("calendar_id", 1); // id, We need to choose from
        // our mobile for primary
        // its 1
        eventValues.put("title", title);
        eventValues.put("description", addInfo);
        eventValues.put("eventLocation", place);

        long endDate = startDate + 1000 * 60 * 60; // For next 1hr

        eventValues.put("dtstart", startDate);
        eventValues.put("dtend", endDate);

        // values.put("allDay", 1); //If it is bithday alarm or such
        // kind (which should remind me for whole day) 0 for false, 1
        // for true
        eventValues.put("eventStatus", status); // This information is
        // sufficient for most
        // entries tentative (0),
        // confirmed (1) or canceled
        // (2):

   /*Comment below visibility and transparency  column to avoid java.lang.IllegalArgumentException column visibility is invalid error */

    /*eventValues.put("visibility", 3); // visibility to default (0),
                                        // confidential (1), private
                                        // (2), or public (3):
    eventValues.put("transparency", 0); // You can control whether
                                        // an event consumes time
                                        // opaque (0) or transparent
                                        // (1).
      */
        eventValues.put("hasAlarm", 1); // 0 for false, 1 for true

        Uri eventUri = curActivity.getApplicationContext().getContentResolver().insert(Uri.parse(eventUriString), eventValues);
        long eventID = Long.parseLong(eventUri.getLastPathSegment());

        if (needReminder) {
            /***************** Event: Reminder(with alert) Adding reminder to event *******************/

            String reminderUriString = "content://com.android.calendar/reminders";

            ContentValues reminderValues = new ContentValues();

            reminderValues.put("event_id", eventID);
            reminderValues.put("minutes", 5); // Default value of the
            // system. Minutes is a
            // integer
            reminderValues.put("method", 1); // Alert Methods: Default(0),
            // Alert(1), Email(2),
            // SMS(3)

            Uri reminderUri = curActivity.getApplicationContext().getContentResolver().insert(Uri.parse(reminderUriString), reminderValues);
        }

        /***************** Event: Meeting(without alert) Adding Attendies to the meeting *******************/

        if (needMailService) {
            String attendeuesesUriString = "content://com.android.calendar/attendees";

            /********
             * To add multiple attendees need to insert ContentValues multiple
             * times
             ***********/
            ContentValues attendeesValues = new ContentValues();

            attendeesValues.put("event_id", eventID);
            attendeesValues.put("attendeeName", "xxxxx"); // Attendees name
            attendeesValues.put("attendeeEmail", "yyyy@gmail.com");// Attendee
            // E
            // mail
            // id
            attendeesValues.put("attendeeRelationship", 0); // Relationship_Attendee(1),
            // Relationship_None(0),
            // Organizer(2),
            // Performer(3),
            // Speaker(4)
            attendeesValues.put("attendeeType", 0); // None(0), Optional(1),
            // Required(2), Resource(3)
            attendeesValues.put("attendeeStatus", 0); // NOne(0), Accepted(1),
            // Decline(2),
            // Invited(3),
            // Tentative(4)

            Uri attendeuesesUri = curActivity.getApplicationContext().getContentResolver().insert(Uri.parse(attendeuesesUriString), attendeesValues);
        }

        return eventID;

    }
//    if (Build.VERSION.SDK_INT >= 14) {
//        Intent intent = new Intent(Intent.ACTION_INSERT)
//                .setData(Events.CONTENT_URI)
//                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
//                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
//                .putExtra(Events.TITLE, "Yoga")
//                .putExtra(Events.DESCRIPTION, "Group class")
//                .putExtra(Events.EVENT_LOCATION, "The gym")
//                .putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY)
//                .putExtra(Intent.EXTRA_EMAIL, "rowan@example.com,trevor@example.com");
//        startActivity(intent);
//    }
//
//    else {
//        Calendar cal = Calendar.getInstance();
//        Intent intent = new Intent(Intent.ACTION_EDIT);
//        intent.setType("vnd.android.cursor.item/event");
//        intent.putExtra("beginTime", cal.getTimeInMillis());
//        intent.putExtra("allDay", true);
//        intent.putExtra("rrule", "FREQ=YEARLY");
//        intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
//        intent.putExtra("title", "A Test Event from android app");
//        startActivity(intent);
//    }

//    Calendar beginTime = Calendar.getInstance();
//    beginTime.set(yearInt, monthInt - 1, dayInt, 7, 30);
//
//
//
//    ContentValues l_event = new ContentValues();
//    l_event.put("calendar_id", CalIds[0]);
//    l_event.put("title", "event");
//    l_event.put("description",  "This is test event");
//    l_event.put("eventLocation", "School");
//    l_event.put("dtstart", beginTime.getTimeInMillis());
//    l_event.put("dtend", beginTime.getTimeInMillis());
//    l_event.put("allDay", 0);
//    l_event.put("rrule", "FREQ=YEARLY");
//    // status: 0~ tentative; 1~ confirmed; 2~ canceled
//    // l_event.put("eventStatus", 1);
//
//    l_event.put("eventTimezone", "India");
//    Uri l_eventUri;
//    if (Build.VERSION.SDK_INT >= 8) {
//        l_eventUri = Uri.parse("content://com.android.calendar/events");
//    } else {
//        l_eventUri = Uri.parse("content://calendar/events");
//    }
//    Uri l_uri = MainActivity.this.getContentResolver()
//            .insert(l_eventUri, l_event);
}
