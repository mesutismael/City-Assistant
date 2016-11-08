package smartassist.appreciate.be.smartassist.model;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import smartassist.appreciate.be.smartassist.database.WeatherTable;

/**
 * Created by Inneke De Clippel on 5/08/2016.
 */
public class Weather
{
    private long date;
    private String currentTemp;
    private String high;
    private int code;

    public long getDate()
    {
        return date;
    }

    public String getCurrentTemp()
    {
        return currentTemp;
    }

    public String getHigh()
    {
        return high;
    }

    public int getCode()
    {
        return code;
    }

    public static List<Weather> constructListFromCursor(Cursor cursor)
    {
        List<Weather> weatherItems = new ArrayList<>();

        if(cursor != null && cursor.moveToFirst())
        {
            do
            {
                weatherItems.add(Weather.constructFromCursor(cursor));
            }
            while (cursor.moveToNext());
        }

        return weatherItems;
    }

    public static Weather constructFromCursor(Cursor cursor)
    {
        Weather weather = new Weather();

        weather.date = cursor.getLong(cursor.getColumnIndex(WeatherTable.COLUMN_DATE));
        weather.currentTemp = cursor.getString(cursor.getColumnIndex(WeatherTable.COLUMN_CURRENT_TEMP));
        weather.high = cursor.getString(cursor.getColumnIndex(WeatherTable.COLUMN_HIGH));
        weather.code = cursor.getInt(cursor.getColumnIndex(WeatherTable.COLUMN_CODE));

        return weather;
    }
}
