package smartassist.appreciate.be.smartassist.database;

import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Banada ismael on 24/10/2016.
 */
public class ResidentNewsTable
{
    public static final String TABLE_NAME = "resident_news";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_RESIDENCE_ID = "residence_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_BODY = "body";
    public static final String COLUMN_FILE = "file";
    public static final String COLUMN_HARD_REMINDER = "hard_reminder";
    public static final String COLUMN_CREATION_DATE = "created_at";
    public static final String COLUMN_LAST_UPDATE = "updated_at";
    public static final String COLUMN_DELETION_DATE = "deleted_at";


    public static final String COLUMN_ID_FULL = TABLE_NAME + "_" + COLUMN_ID;
    public static final String COLUMN_RESIDENCE_ID_FULL = TABLE_NAME + "_" + COLUMN_RESIDENCE_ID;
    public static final String COLUMN_TITLE_FULL = TABLE_NAME + "_" + COLUMN_TITLE;
    public static final String COLUMN_BODY_FULL = TABLE_NAME + "_" + COLUMN_BODY;
    public static final String COLUMN_HARD_REMINDER_FULL = TABLE_NAME + "_" + COLUMN_HARD_REMINDER;
    public static final String COLUMN_FILE_FULL = TABLE_NAME + "_" + COLUMN_FILE;
    public static final String COLUMN_CREATION_DATE_FULL = TABLE_NAME + "_" + COLUMN_CREATION_DATE;
    public static final String COLUMN_LAST_UPDATE_FULL = TABLE_NAME + "_" + COLUMN_LAST_UPDATE;
    public static final String COLUMN_DELETION_DATE_FULL = TABLE_NAME + "_" + COLUMN_DELETION_DATE;

    public static final Map<String, String> PROJECTION_MAP;

    static
    {
        PROJECTION_MAP = new HashMap<>();
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_ID, TABLE_NAME + "." + COLUMN_ID + " AS " + COLUMN_ID_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_RESIDENCE_ID, TABLE_NAME + "." + COLUMN_RESIDENCE_ID + " AS " + COLUMN_RESIDENCE_ID_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_TITLE, TABLE_NAME + "." + COLUMN_TITLE + " AS " + COLUMN_TITLE_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_BODY, TABLE_NAME + "." + COLUMN_BODY + " AS " + COLUMN_BODY_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_FILE, TABLE_NAME + "." + COLUMN_FILE + " AS " + COLUMN_FILE_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_HARD_REMINDER, TABLE_NAME + "." + COLUMN_HARD_REMINDER + " AS " + COLUMN_HARD_REMINDER_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_CREATION_DATE, TABLE_NAME + "." + COLUMN_CREATION_DATE + " AS " + COLUMN_CREATION_DATE_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_LAST_UPDATE, TABLE_NAME + "." + COLUMN_LAST_UPDATE + " AS " + COLUMN_LAST_UPDATE_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_DELETION_DATE, TABLE_NAME + "." + COLUMN_DELETION_DATE + " AS " + COLUMN_DELETION_DATE_FULL);

    }

    private static final String CREATE_TABLE = "create table IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_RESIDENCE_ID + " integer, "
            + COLUMN_TITLE + " text, "
            + COLUMN_BODY + " text, "
            + COLUMN_FILE + " text, "
            + COLUMN_HARD_REMINDER + " int, "
            + COLUMN_CREATION_DATE + " long, "
            + COLUMN_LAST_UPDATE + " long, "
            + COLUMN_DELETION_DATE + " long "
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
