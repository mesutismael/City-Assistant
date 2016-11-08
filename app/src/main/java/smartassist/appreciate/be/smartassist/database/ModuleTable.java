package smartassist.appreciate.be.smartassist.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Inneke on 30/01/2015.
 */
public class ModuleTable
{
    public static final String TABLE_NAME = "modules";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_MODULE_ID = "module_id";
    public static final String COLUMN_ROW_PORTRAIT = "row_portrait";
    public static final String COLUMN_COLUMN_PORTRAIT = "column_portrait";
    public static final String COLUMN_ROW_SPAN_PORTRAIT = "row_span_portrait";
    public static final String COLUMN_COLUMN_SPAN_PORTRAIT = "column_span_portrait";
    public static final String COLUMN_ROW_LAND = "row_land";
    public static final String COLUMN_COLUMN_LAND = "column_land";
    public static final String COLUMN_ROW_SPAN_LAND = "row_span_land";
    public static final String COLUMN_COLUMN_SPAN_LAND = "column_span_land";
    public static final String COLUMN_LAST_UPDATE = "last_update";

    private static final String CREATE_TABLE = "create table IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_MODULE_ID + " integer, "
            + COLUMN_ROW_PORTRAIT + " integer, "
            + COLUMN_COLUMN_PORTRAIT + " integer, "
            + COLUMN_ROW_SPAN_PORTRAIT + " integer, "
            + COLUMN_COLUMN_SPAN_PORTRAIT + " integer, "
            + COLUMN_ROW_LAND + " integer, "
            + COLUMN_COLUMN_LAND + " integer, "
            + COLUMN_ROW_SPAN_LAND + " integer, "
            + COLUMN_COLUMN_SPAN_LAND + " integer, "
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
