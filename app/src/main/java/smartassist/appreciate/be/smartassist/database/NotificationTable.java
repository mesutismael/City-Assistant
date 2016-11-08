package smartassist.appreciate.be.smartassist.database;

import android.database.sqlite.SQLiteDatabase;

import smartassist.appreciate.be.smartassist.utils.NotificationHelper;

/**
 * Created by Inneke on 16/03/2015.
 */
public class NotificationTable
{
    public static final String TABLE_NAME = "notifications";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ITEM_ID = "item_id";
    public static final String COLUMN_EXTRA_ID = "extra_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_HARD_REMINDER = "hard_reminder";
    public static final String COLUMN_LAST_UPDATE = "last_update";

    private static final String CREATE_TABLE = "create table IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_ITEM_ID + " integer, "
            + COLUMN_EXTRA_ID + " integer, "
            + COLUMN_TITLE + " text, "
            + COLUMN_DESCRIPTION + " text, "
            + COLUMN_TYPE + " integer, "
            + COLUMN_HARD_REMINDER + " integer, "
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
                    //All chat notifications need to be cleared
                    database.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_TYPE + " = " + NotificationHelper.TYPE_CHAT);
                    break;
            }

            oldVersion++;
        }
    }
}
