package smartassist.appreciate.be.smartassist.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Inneke on 25/02/2015.
 */
public class RssTable
{
    public static final String TABLE_NAME = "rss";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_RSS_ID = "rss_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_INTRO = "intro";
    public static final String COLUMN_BODY = "body";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_LAST_UPDATE = "last_update";

    private static final String CREATE_TABLE = "create table IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_RSS_ID + " integer, "
            + COLUMN_TITLE + " text, "
            + COLUMN_INTRO + " text, "
            + COLUMN_BODY + " text, "
            + COLUMN_IMAGE + " text, "
            + COLUMN_DATE + " long, "
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
