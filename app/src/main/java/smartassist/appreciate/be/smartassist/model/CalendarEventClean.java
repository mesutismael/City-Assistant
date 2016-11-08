package smartassist.appreciate.be.smartassist.model;

import android.database.Cursor;

import smartassist.appreciate.be.smartassist.database.CalendarEventTable;

/**
 * Created by Inneke on 20/04/2015.
 */
public class CalendarEventClean implements Comparable<CalendarEventClean>
{
    private int id;
    private String description;
    private long start;
    private long stop;
    private int level;
    private int repeatCode;
    private int repeatAmount;
    private long repeatUntil;

    public CalendarEventClean()
    {
    }

    public CalendarEventClean(int id, String description, long start, long stop, int level, int repeatCode, int repeatAmount, long repeatUntil)
    {
        this.id = id;
        this.description = description;
        this.start = start;
        this.stop = stop;
        this.level = level;
        this.repeatCode = repeatCode;
        this.repeatAmount = repeatAmount;
        this.repeatUntil = repeatUntil;
    }

    public int getId()
    {
        return id;
    }

    public String getDescription()
    {
        return description;
    }

    public long getStart()
    {
        return start;
    }

    public long getStop()
    {
        return stop;
    }

    public int getLevel()
    {
        return level;
    }

    public int getRepeatCode()
    {
        return repeatCode;
    }

    public int getRepeatAmount()
    {
        return repeatAmount;
    }

    public long getRepeatUntil()
    {
        return repeatUntil;
    }

    @Override
    public int compareTo(CalendarEventClean another)
    {
        if(another == null)
            return -1;
        return this.getStart() < another.getStart() ? -1 : this.getStart() > another.getStart() ? 1 : 0;
    }

    public static CalendarEventClean constructFromCursor(Cursor cursor)
    {
        CalendarEventClean event = new CalendarEventClean();

        event.id = cursor.getInt(cursor.getColumnIndex(CalendarEventTable.COLUMN_EVENT_ID));
        event.description = cursor.getString(cursor.getColumnIndex(CalendarEventTable.COLUMN_DESCRIPTION));
        event.start = cursor.getLong(cursor.getColumnIndex(CalendarEventTable.COLUMN_START));
        event.stop = cursor.getLong(cursor.getColumnIndex(CalendarEventTable.COLUMN_STOP));
        event.level = cursor.getInt(cursor.getColumnIndex(CalendarEventTable.COLUMN_LEVEL));
        event.repeatCode = cursor.getInt(cursor.getColumnIndex(CalendarEventTable.COLUMN_REPEAT_CODE));
        event.repeatAmount = cursor.getInt(cursor.getColumnIndex(CalendarEventTable.COLUMN_REPEAT_AMOUNT));
        event.repeatUntil = cursor.getLong(cursor.getColumnIndex(CalendarEventTable.COLUMN_REPEAT_UNTIL));

        return event;
    }
}
