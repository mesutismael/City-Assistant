package smartassist.appreciate.be.smartassist.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Inneke on 20/02/2015.
 */
public class CalendarBirthdayTable
{
    public static final String TABLE_NAME = "calendar_birthdays";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_BIRTHDAY_ID = "birthday_id";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_MONTH = "month";
    public static final String COLUMN_DAY = "day";
    public static final String COLUMN_LAST_UPDATE = "last_update";

    private static final String CREATE_TABLE = "create table IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_BIRTHDAY_ID + " integer, "
            + COLUMN_FIRST_NAME + " text, "
            + COLUMN_LAST_NAME + " text, "
            + COLUMN_YEAR + " integer, "
            + COLUMN_MONTH + " integer, "
            + COLUMN_DAY + " integer, "
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
