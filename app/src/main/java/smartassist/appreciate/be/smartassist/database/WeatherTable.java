package smartassist.appreciate.be.smartassist.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Inneke on 23/02/2015.
 */
public class WeatherTable
{
    public static final String TABLE_NAME = "weather";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CURRENT_TEMP = "current_temp";
    public static final String COLUMN_HIGH = "high";
    public static final String COLUMN_LOW = "low";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_CODE = "code";
    public static final String COLUMN_LAST_UPDATE = "last_update";

    private static final String CREATE_TABLE = "create table IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_CURRENT_TEMP + " text, "
            + COLUMN_HIGH + " text, "
            + COLUMN_LOW + " text, "
            + COLUMN_DATE + " long, "
            + COLUMN_CODE + " integer, "
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
