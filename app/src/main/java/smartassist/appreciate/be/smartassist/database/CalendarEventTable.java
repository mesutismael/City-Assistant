package smartassist.appreciate.be.smartassist.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Inneke on 20/02/2015.
 */
public class CalendarEventTable
{
    public static final String TABLE_NAME = "calendar_events";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_EVENT_ID = "event_id";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_START = "start_date";
    public static final String COLUMN_STOP = "stop_date";
    public static final String COLUMN_ALARM = "alarm_date";
    public static final String COLUMN_ALARM_FINISHED = "alarm_finished";
    public static final String COLUMN_LEVEL = "level";
    public static final String COLUMN_LAST_UPDATE = "last_update";
    public static final String COLUMN_REPEAT_CODE = "repeat_code";
    public static final String COLUMN_REPEAT_AMOUNT = "repeat_amount";
    public static final String COLUMN_REPEAT_UNTIL = "repeat_until";

    private static final String CREATE_TABLE = "create table IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_EVENT_ID + " integer, "
            + COLUMN_DESCRIPTION + " text, "
            + COLUMN_START + " long, "
            + COLUMN_STOP + " long, "
            + COLUMN_ALARM + " long, "
            + COLUMN_ALARM_FINISHED + " integer, "
            + COLUMN_LEVEL + " integer, "
            + COLUMN_LAST_UPDATE + " long, "
            + COLUMN_REPEAT_CODE + " integer, "
            + COLUMN_REPEAT_AMOUNT + " integer, "
            + COLUMN_REPEAT_UNTIL + " long"
            + ");";

    public static void onCreate(SQLiteDatabase database)
    {
        database.execSQL(CREATE_TABLE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        while (oldVersion < newVersion)
        {
            switch (oldVersion)
            {
                case 1:
                    //No changes
                    break;
            }

            oldVersion++;
        }
    }
}
