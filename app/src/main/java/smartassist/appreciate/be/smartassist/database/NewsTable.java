package smartassist.appreciate.be.smartassist.database;

import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Inneke on 23/02/2015.
 */
public class NewsTable
{
    public static final String TABLE_NAME = "news";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NEWS_ID = "news_id";
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_BODY = "body";
    public static final String COLUMN_PERIOD = "period";
    public static final String COLUMN_START_DATE = "start_date";
    public static final String COLUMN_END_DATE = "end_date";
    public static final String COLUMN_CREATION_DATE = "creation_date";
    public static final String COLUMN_RANDOM_PHOTO = "random_photo";
    public static final String COLUMN_LAST_UPDATE = "last_update";

    public static final String COLUMN_ID_FULL = TABLE_NAME + "_" + COLUMN_ID;
    public static final String COLUMN_NEWS_ID_FULL = TABLE_NAME + "_" + COLUMN_NEWS_ID;
    public static final String COLUMN_CATEGORY_ID_FULL = TABLE_NAME + "_" + COLUMN_CATEGORY_ID;
    public static final String COLUMN_TITLE_FULL = TABLE_NAME + "_" + COLUMN_TITLE;
    public static final String COLUMN_BODY_FULL = TABLE_NAME + "_" + COLUMN_BODY;
    public static final String COLUMN_PERIOD_FULL = TABLE_NAME + "_" + COLUMN_PERIOD;
    public static final String COLUMN_START_DATE_FULL = TABLE_NAME + "_" + COLUMN_START_DATE;
    public static final String COLUMN_END_DATE_FULL = TABLE_NAME + "_" + COLUMN_END_DATE;
    public static final String COLUMN_CREATION_DATE_FULL = TABLE_NAME + "_" + COLUMN_CREATION_DATE;
    public static final String COLUMN_RANDOM_PHOTO_FULL = TABLE_NAME + "_" + COLUMN_RANDOM_PHOTO;
    public static final String COLUMN_LAST_UPDATE_FULL = TABLE_NAME + "_" + COLUMN_LAST_UPDATE;

    public static final Map<String, String> PROJECTION_MAP;

    static
    {
        PROJECTION_MAP = new HashMap<>();
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_ID, TABLE_NAME + "." + COLUMN_ID + " AS " + COLUMN_ID_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_NEWS_ID, TABLE_NAME + "." + COLUMN_NEWS_ID + " AS " + COLUMN_NEWS_ID_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_CATEGORY_ID, TABLE_NAME + "." + COLUMN_CATEGORY_ID + " AS " + COLUMN_CATEGORY_ID_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_TITLE, TABLE_NAME + "." + COLUMN_TITLE + " AS " + COLUMN_TITLE_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_BODY, TABLE_NAME + "." + COLUMN_BODY + " AS " + COLUMN_BODY_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_PERIOD, TABLE_NAME + "." + COLUMN_PERIOD + " AS " + COLUMN_PERIOD_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_START_DATE, TABLE_NAME + "." + COLUMN_START_DATE + " AS " + COLUMN_START_DATE_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_END_DATE, TABLE_NAME + "." + COLUMN_END_DATE + " AS " + COLUMN_END_DATE_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_CREATION_DATE, TABLE_NAME + "." + COLUMN_CREATION_DATE + " AS " + COLUMN_CREATION_DATE_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_RANDOM_PHOTO, TABLE_NAME + "." + COLUMN_RANDOM_PHOTO + " AS " + COLUMN_RANDOM_PHOTO_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_LAST_UPDATE, TABLE_NAME + "." + COLUMN_LAST_UPDATE + " AS " + COLUMN_LAST_UPDATE_FULL);
    }

    private static final String CREATE_TABLE = "create table IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NEWS_ID + " integer, "
            + COLUMN_CATEGORY_ID + " integer, "
            + COLUMN_TITLE + " text, "
            + COLUMN_BODY + " text, "
            + COLUMN_PERIOD + " text, "
            + COLUMN_START_DATE + " long, "
            + COLUMN_END_DATE + " long, "
            + COLUMN_CREATION_DATE + " long, "
            + COLUMN_RANDOM_PHOTO + " real, "
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
