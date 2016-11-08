package smartassist.appreciate.be.smartassist.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Inneke on 10/02/2015.
 */
public class OpeningTimesTable
{
    public static final String TABLE_NAME = "opening_times";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_OPENING_TIME_ID = "opening_time_id";
    public static final String COLUMN_POI_ID = "poi_id";
    public static final String COLUMN_DAY = "day";
    public static final String COLUMN_START_MORNING = "start_morning";
    public static final String COLUMN_STOP_MORNING = "stop_morning";
    public static final String COLUMN_START_MIDDAY = "start_midday";
    public static final String COLUMN_STOP_MIDDAY = "stop_midday";
    public static final String COLUMN_LAST_UPDATE = "last_update";

    public static final String COLUMN_ID_FULL = TABLE_NAME + "_" + COLUMN_ID;

    private static final String CREATE_TABLE = "create table IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_OPENING_TIME_ID + " integer, "
            + COLUMN_POI_ID + " integer, "
            + COLUMN_DAY + " integer, "
            + COLUMN_START_MORNING + " text, "
            + COLUMN_STOP_MORNING + " text, "
            + COLUMN_START_MIDDAY + " text, "
            + COLUMN_STOP_MIDDAY + " text, "
            + COLUMN_LAST_UPDATE + " long"
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
