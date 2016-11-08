package smartassist.appreciate.be.smartassist.model;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.database.OpeningTimesTable;

/**
 * Created by Inneke De Clippel on 5/08/2016.
 */
public class OpeningTime
{
    private int day;
    private String startMorning;
    private String stopMorning;
    private String startMidday;
    private String stopMidday;

    public int getDay()
    {
        return day;
    }

    public String getStartMorning()
    {
        return startMorning;
    }

    public String getStopMorning()
    {
        return stopMorning;
    }

    public String getStartMidday()
    {
        return startMidday;
    }

    public String getStopMidday()
    {
        return stopMidday;
    }

    public int getDayResource()
    {
        switch (this.day)
        {
            case 0: return R.string.poi_monday;
            case 1: return R.string.poi_tuesday;
            case 2: return R.string.poi_wednesday;
            case 3: return R.string.poi_thursday;
            case 4: return R.string.poi_friday;
            case 5: return R.string.poi_saturday;
            case 6: return R.string.poi_sunday;
            default: return R.string.poi_sunday;
        }
    }

    public static List<OpeningTime> constructListFromCursor(Cursor cursor)
    {
        List<OpeningTime> openingTimes = new ArrayList<>();

        if(cursor != null && cursor.moveToFirst())
        {
            do
            {
                openingTimes.add(OpeningTime.constructFromCursor(cursor));
            }
            while (cursor.moveToNext());
        }

        return openingTimes;
    }

    public static OpeningTime constructFromCursor(Cursor cursor)
    {
        OpeningTime openingTime = new OpeningTime();

        openingTime.day = cursor.getInt(cursor.getColumnIndex(OpeningTimesTable.COLUMN_DAY));
        openingTime.startMorning = cursor.getString(cursor.getColumnIndex(OpeningTimesTable.COLUMN_START_MORNING));
        openingTime.stopMorning = cursor.getString(cursor.getColumnIndex(OpeningTimesTable.COLUMN_STOP_MORNING));
        openingTime.startMidday = cursor.getString(cursor.getColumnIndex(OpeningTimesTable.COLUMN_START_MIDDAY));
        openingTime.stopMidday = cursor.getString(cursor.getColumnIndex(OpeningTimesTable.COLUMN_STOP_MIDDAY));

        return openingTime;
    }
}
