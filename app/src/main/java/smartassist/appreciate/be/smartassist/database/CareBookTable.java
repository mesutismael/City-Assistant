package smartassist.appreciate.be.smartassist.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Inneke De Clippel on 15/02/2016.
 */
public class CareBookTable
{
    public static final String TABLE_NAME = "carebook";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CAREBOOK_ID = "carebook_id";
    public static final String COLUMN_SMILEY = "smiley";
    public static final String COLUMN_MESSAGE = "message";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_LAST_UPDATE = "last_update";

    private static final String CREATE_TABLE = "create table IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_CAREBOOK_ID + " integer, "
            + COLUMN_SMILEY + " integer, "
            + COLUMN_MESSAGE + " text, "
            + COLUMN_AUTHOR + " text, "
            + COLUMN_CREATED_AT + " long, "
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
