package smartassist.appreciate.be.smartassist.utils;

import android.content.Context;
import android.database.Cursor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.model.CalendarEventClean;

/**
 * Created by Inneke on 23/02/2015.
 */
public class CalendarUtils
{
    public static final int REMINDER_NEVER = 0;
    public static final int REMINDER_5 = 1;
    public static final int REMINDER_10 = 2;
    public static final int REMINDER_15 = 3;
    public static final int REMINDER_20 = 4;
    public static final int REMINDER_25 = 5;
    public static final int REMINDER_30 = 6;
    public static final int REMINDER_45 = 7;
    public static final int REMINDER_HOUR = 8;
    public static final int REMINDER_2_HOUR = 9;
    public static final int REMINDER_3_HOUR = 10;
    public static final int REMINDER_12_HOUR = 11;
    public static final int REMINDER_DAY = 12;
    public static final int REMINDER_2_DAY = 13;
    public static final int REMINDER_WEEK = 14;

    public static final int REPEAT_NEVER = 1;
    public static final int REPEAT_DAILY = 2;
    public static final int REPEAT_WEEKLY = 3;
    public static final int REPEAT_MONTHLY = 4;

    private static final SimpleDateFormat SDF_HOUR = new SimpleDateFormat("H:mm", Locale.getDefault());

    public static long getAlarmTime(long start, int reminder)
    {
        if(start <= System.currentTimeMillis())
            return 0;

        switch (reminder)
        {
            case REMINDER_NEVER: return 0;
            case REMINDER_5: return start - 5 * 60 * 1000;
            case REMINDER_10: return start - 10 * 60 * 1000;
            case REMINDER_15: return start - 15 * 60 * 1000;
            case REMINDER_20: return start - 20 * 60 * 1000;
            case REMINDER_25: return start - 25 * 60 * 1000;
            case REMINDER_30: return start - 30 * 60 * 1000;
            case REMINDER_45: return start - 45 * 60 * 1000;
            case REMINDER_HOUR: return start - 60 * 60 * 1000;
            case REMINDER_2_HOUR: return start - 2 * 60 * 60 * 1000;
            case REMINDER_3_HOUR: return start - 3 * 60 * 60 * 1000;
            case REMINDER_12_HOUR: return start - 12 * 60 * 60 * 1000;
            case REMINDER_DAY: return start - 24 * 60 * 60 * 1000;
            case REMINDER_2_DAY: return start - 2 * 24 * 60 * 60 * 1000;
            case REMINDER_WEEK: return start - 7 * 24 * 60 * 60 * 1000;
            default: return 0;
        }
    }

    public static List<CalendarEventClean> constructEventsFromCursor(Cursor cursor, long intervalStart, long intervalStop)
    {
        List<CalendarEventClean> events = new ArrayList<>();

        if(cursor != null)
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                CalendarEventClean event = CalendarEventClean.constructFromCursor(cursor);

                switch (event.getRepeatCode())
                {
                    case REPEAT_NEVER:
                        if(event.getStart() < intervalStop && event.getStop() > intervalStart)
                        {
                            //The event happens in the given interval
                            events.addAll(duplicateEventWithMultipleDays(event, intervalStart, intervalStop));
                        }
                        break;

                    case REPEAT_DAILY:
                    case REPEAT_WEEKLY:
                    case REPEAT_MONTHLY:
                        if(event.getStart() < intervalStop && event.getRepeatUntil() > intervalStart)
                        {
                            //The event or one of its repeats happens in the given interval
                            List<CalendarEventClean> repeats = duplicateEventWithRepeat(event, intervalStart, intervalStop);
                            for(CalendarEventClean repeat : repeats)
                            {
                                events.addAll(duplicateEventWithMultipleDays(repeat, intervalStart, intervalStop));
                            }
                        }
                        break;
                }

                cursor.moveToNext();
            }
        }

        return events;
    }

    private static List<CalendarEventClean> duplicateEventWithMultipleDays(CalendarEventClean event, long intervalStart, long intervalStop)
    {
        List<CalendarEventClean> events = new ArrayList<>();

        if(event != null)
        {
            Calendar calendarStart = Calendar.getInstance();
            calendarStart.setTimeInMillis(event.getStart());
            Calendar calendarStop = Calendar.getInstance();
            calendarStop.setTimeInMillis(event.getStop());

            while(calendarStart.get(Calendar.DAY_OF_YEAR) != calendarStop.get(Calendar.DAY_OF_YEAR) || calendarStart.get(Calendar.YEAR) != calendarStop.get(Calendar.YEAR))
            {
                long start = calendarStart.getTimeInMillis();
                calendarStart.set(Calendar.HOUR_OF_DAY, 23);
                calendarStart.set(Calendar.MINUTE, 59);
                long stop = calendarStart.getTimeInMillis();

                if(start < intervalStop && stop > intervalStart)
                    events.add(new CalendarEventClean(event.getId(), event.getDescription(), start, stop, event.getLevel(), event.getRepeatCode(), event.getRepeatAmount(), event.getRepeatUntil()));

                calendarStart.add(Calendar.DAY_OF_YEAR, 1);
                calendarStart.set(Calendar.HOUR_OF_DAY, 0);
                calendarStart.set(Calendar.MINUTE, 0);
            }

            long start = calendarStart.getTimeInMillis();
            long stop = calendarStop.getTimeInMillis();
            if(start < intervalStop && stop > intervalStart)
                events.add(new CalendarEventClean(event.getId(), event.getDescription(), start, stop, event.getLevel(), event.getRepeatCode(), event.getRepeatAmount(), event.getRepeatUntil()));
        }

        return events;
    }

    private static List<CalendarEventClean> duplicateEventWithRepeat(CalendarEventClean event, long intervalStart, long intervalStop)
    {
        List<CalendarEventClean> events = new ArrayList<>();

        if(event != null)
        {
            int calendarField;
            switch (event.getRepeatCode())
            {
                case REPEAT_DAILY: calendarField = Calendar.DAY_OF_YEAR; break;
                case REPEAT_WEEKLY: calendarField = Calendar.WEEK_OF_YEAR; break;
                case REPEAT_MONTHLY: calendarField = Calendar.MONTH; break;
                default: calendarField = Calendar.MONTH; break;
            }

            Calendar calendarStart = Calendar.getInstance();
            calendarStart.setTimeInMillis(event.getStart());
            Calendar calendarStop = Calendar.getInstance();
            calendarStop.setTimeInMillis(event.getStop());

            while (calendarStop.getTimeInMillis() < event.getRepeatUntil() && calendarStart.getTimeInMillis() < intervalStop)
            {
                long start = calendarStart.getTimeInMillis();
                long stop = calendarStop.getTimeInMillis();

                if(start < intervalStop && stop > intervalStart)
                    events.add(new CalendarEventClean(event.getId(), event.getDescription(), start, stop, event.getLevel(), event.getRepeatCode(), event.getRepeatAmount(), event.getRepeatUntil()));

                calendarStart.add(calendarField, event.getRepeatAmount());
                calendarStop.add(calendarField, event.getRepeatAmount());
            }
        }

        return events;
    }

    public static String getEventTimeText(Context context, long start, long stop)
    {
        String startHour = SDF_HOUR.format(new Date(start));
        String stopHour = SDF_HOUR.format(new Date(stop));
        StringBuilder stringBuilder = new StringBuilder(startHour);
        stringBuilder.append(" - ").append(stopHour);
        String formattedTime = stringBuilder.toString();

        if(formattedTime.equals("0:00 - 23:59"))
            return context.getString(R.string.calendar_entire_day);
        return formattedTime;
    }
}
