package smartassist.appreciate.be.smartassist.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Inneke on 3/03/2015.
 */
public class PhotoTable
{
    public static final String TABLE_NAME = "photos";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PHOTO_ID = "photo_id";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_IMAGE = "image_url";
    public static final String COLUMN_THUMBNAIL = "thumbnail_url";
    public static final String COLUMN_SCREEN_SAVER = "screen_saver";
    public static final String COLUMN_LAST_UPDATE = "last_update";

    private static final String CREATE_TABLE = "create table IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_PHOTO_ID + " integer, "
            + COLUMN_DESCRIPTION + " text, "
            + COLUMN_IMAGE + " text, "
            + COLUMN_THUMBNAIL + " text, "
            + COLUMN_SCREEN_SAVER + " integer, "
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
