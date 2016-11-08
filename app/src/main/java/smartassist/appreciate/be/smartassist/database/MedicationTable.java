package smartassist.appreciate.be.smartassist.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Inneke De Clippel on 25/04/2016.
 */
public class MedicationTable
{
    public static final String TABLE_NAME = "medication";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_MEDICATION_ID = "medication_id";
    public static final String COLUMN_HABITANT_ID = "habitant_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_START_DATE = "start_date";
    public static final String COLUMN_END_DATE = "end_date";
    public static final String COLUMN_DOSE = "dose";
    public static final String COLUMN_TIME_1 = "time_1";
    public static final String COLUMN_TIME_2 = "time_2";
    public static final String COLUMN_TIME_3 = "time_3";
    public static final String COLUMN_TIME_4 = "time_4";
    public static final String COLUMN_TIME_5 = "time_5";
    public static final String COLUMN_TIME_6 = "time_6";
    public static final String COLUMN_TIME_7 = "time_7";
    public static final String COLUMN_DAYS = "days";
    public static final String COLUMN_LAST_UPDATE = "last_update";

    private static final String CREATE_TABLE = "create table IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_MEDICATION_ID + " integer, "
            + COLUMN_HABITANT_ID + " integer, "
            + COLUMN_NAME + " text, "
            + COLUMN_START_DATE + " long, "
            + COLUMN_END_DATE + " long, "
            + COLUMN_DOSE + " text, "
            + COLUMN_TIME_1 + " integer, "
            + COLUMN_TIME_2 + " integer, "
            + COLUMN_TIME_3 + " integer, "
            + COLUMN_TIME_4 + " integer, "
            + COLUMN_TIME_5 + " integer, "
            + COLUMN_TIME_6 + " integer, "
            + COLUMN_TIME_7 + " integer, "
            + COLUMN_DAYS + " text, "
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
