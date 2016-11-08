package smartassist.appreciate.be.smartassist.utils;

import android.content.Context;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import smartassist.appreciate.be.smartassist.R;

/**
 * Created by Inneke De Clippel on 18/04/2016.
 */
public class DateUtils
{
    private static final SimpleDateFormat SDF_CHAT = new SimpleDateFormat("d-MM-''yy HH:mm", Locale.getDefault());
    private static final SimpleDateFormat SDF_CAREBOOK = new SimpleDateFormat("EEEE d MMMM yyyy", Locale.getDefault());
    private static final SimpleDateFormat SDF_HABITANT = new SimpleDateFormat("d-MM-yyyy", Locale.getDefault());
    private static final SimpleDateFormat SDF_WEATHER_DAY_OF_WEEK = new SimpleDateFormat("EE", Locale.getDefault());
    private static final SimpleDateFormat SDF_NEWS_CALENDAR = new SimpleDateFormat("H:mm", Locale.getDefault());
    private static final SimpleDateFormat SDF_NEWS = new SimpleDateFormat("d MMMM", Locale.getDefault());
    private static final SimpleDateFormat SDF_OPENING_TIME = new SimpleDateFormat("H:mm", Locale.getDefault());
    private static final SimpleDateFormat SDF_RSS = new SimpleDateFormat("d MMMM", Locale.getDefault());
    private static final SimpleDateFormat SDF_API = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private static final SimpleDateFormat SDF_API_MINUTES = new SimpleDateFormat("yyyy-MM-dd H:mm", Locale.getDefault());
    private static final SimpleDateFormat SDF_API_SECONDS = new SimpleDateFormat("yyyy-MM-dd H:mm:ss", Locale.getDefault());
    private static final SimpleDateFormat SDF_API_MEDICATION = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private static final SimpleDateFormat SDF_API_WEATHER = new SimpleDateFormat("yyyy-MM-dd'T'H:mm:ssZ", Locale.getDefault());
    private static final SimpleDateFormat SDF_API_OPENING_TIME = new SimpleDateFormat("H:mm:ss", Locale.getDefault());

    static
    {
        SDF_API.setTimeZone(Constants.TIME_ZONE_BACKEND);
        SDF_API_MINUTES.setTimeZone(Constants.TIME_ZONE_BACKEND);
        SDF_API_SECONDS.setTimeZone(Constants.TIME_ZONE_BACKEND);
        SDF_API_MEDICATION.setTimeZone(Constants.TIME_ZONE_BACKEND);
        SDF_API_OPENING_TIME.setTimeZone(Constants.TIME_ZONE_BACKEND);
    }

    private static long parseDate(String date, SimpleDateFormat format)
    {
        if(!TextUtils.isEmpty(date))
        {
            try
            {
                return format.parse(date).getTime();
            }
            catch (ParseException e)
            {
                return 0;
            }
        }
        else
        {
            return 0;
        }
    }

    public static String formatChatDate(long millis)
    {
        return millis != 0 ? SDF_CHAT.format(new Date(millis)) : null;
    }

    public static String formatCarebookDate(long millis, Context context)
    {
        if(android.text.format.DateUtils.isToday(millis))
        {
            return context.getString(R.string.care_book_today);
        }
        else if(android.text.format.DateUtils.isToday(millis + TimeUnit.DAYS.toMillis(1)))
        {
            return context.getString(R.string.care_book_yesterday);
        }
        else
        {
            return SDF_CAREBOOK.format(new Date(millis));
        }
    }

    public static String formatHabitantDate(long millis)
    {
        return millis != 0 ? SDF_HABITANT.format(new Date(millis)) : null;
    }

    public static String formatWeatherDayOfWeek(long millis)
    {
        return millis != 0 ? SDF_WEATHER_DAY_OF_WEEK.format(new Date(millis)).toUpperCase() : null;
    }

    public static String formatNewsCalendar(long millis)
    {
        return millis != 0 ? SDF_NEWS_CALENDAR.format(new Date(millis)) : null;
    }

    public static String formatNewsDate(long millis)
    {
        return millis != 0 ? SDF_NEWS.format(new Date(millis)) : null;
    }

    public static String formatOpeningTime(long millis)
    {
        return millis != 0 ? SDF_OPENING_TIME.format(new Date(millis)) : null;
    }

    public static String formatRssDate(long millis)
    {
        return millis != 0 ? SDF_RSS.format(new Date(millis)) : null;
    }

    public static String formatApiDateSeconds(long millis)
    {
        return SDF_API_SECONDS.format(new Date(millis));
    }

    public static long parseApiDate(String date)
    {
        return DateUtils.parseDate(date, SDF_API);
    }

    public static long parseApiDateMinutes(String date)
    {
        return DateUtils.parseDate(date, SDF_API_MINUTES);
    }

    public static long parseApiDateSeconds(String date)
    {
        return DateUtils.parseDate(date, SDF_API_SECONDS);
    }

    public static long parseApiDateMedication(String date)
    {
        return DateUtils.parseDate(date, SDF_API_MEDICATION);
    }

    public static long parseApiDateWeather(String date)
    {
        return DateUtils.parseDate(date, SDF_API_WEATHER);
    }

    public static long parseApiOpeningTime(String date)
    {
        return DateUtils.parseDate(date, SDF_API_OPENING_TIME);
    }

    public static long getEndOfDay(long millis)
    {
        if(millis != 0)
        {
            Calendar calendarEnd = Calendar.getInstance();
            calendarEnd.setTimeInMillis(millis);
            calendarEnd.set(Calendar.HOUR_OF_DAY, 23);
            calendarEnd.set(Calendar.MINUTE, 59);
            calendarEnd.set(Calendar.SECOND, 59);
            calendarEnd.set(Calendar.MILLISECOND, 999);
            return calendarEnd.getTimeInMillis();
        }
        else
        {
            return 0;
        }
    }

    public static Calendar getCalendar(int daysToAdd)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, daysToAdd);
        return calendar;
    }

    public static boolean isSameDay(long millis1, long millis2)
    {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(millis1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(millis2);

        return DateUtils.isSameDay(cal1, cal2);
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2)
    {
        return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
    }
}
