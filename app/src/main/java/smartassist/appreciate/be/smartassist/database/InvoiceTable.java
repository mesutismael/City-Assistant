package smartassist.appreciate.be.smartassist.database;

import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Inneke De Clippel on 1/08/2016.
 */
public class InvoiceTable
{
    public static final String TABLE_NAME = "invoices";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_INVOICE_ID = "invoice_id";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_LAST_UPDATE = "last_update";

    public static final String COLUMN_ID_FULL = TABLE_NAME + "_" + COLUMN_ID;
    public static final String COLUMN_INVOICE_ID_FULL = TABLE_NAME + "_" + COLUMN_INVOICE_ID;
    public static final String COLUMN_NUMBER_FULL = TABLE_NAME + "_" + COLUMN_NUMBER;
    public static final String COLUMN_DATE_FULL = TABLE_NAME + "_" + COLUMN_DATE;
    public static final String COLUMN_URL_FULL = TABLE_NAME + "_" + COLUMN_URL;
    public static final String COLUMN_LAST_UPDATE_FULL = TABLE_NAME + "_" + COLUMN_LAST_UPDATE;

    public static final Map<String, String> PROJECTION_MAP;

    static
    {
        PROJECTION_MAP = new HashMap<>();
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_ID, TABLE_NAME + "." + COLUMN_ID + " AS " + COLUMN_ID_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_INVOICE_ID, TABLE_NAME + "." + COLUMN_INVOICE_ID + " AS " + COLUMN_INVOICE_ID_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_NUMBER, TABLE_NAME + "." + COLUMN_NUMBER + " AS " + COLUMN_NUMBER_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_DATE, TABLE_NAME + "." + COLUMN_DATE + " AS " + COLUMN_DATE_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_URL, TABLE_NAME + "." + COLUMN_URL + " AS " + COLUMN_URL_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_LAST_UPDATE, TABLE_NAME + "." + COLUMN_LAST_UPDATE + " AS " + COLUMN_LAST_UPDATE_FULL);
    }

    private static final String CREATE_TABLE = "create table IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_INVOICE_ID + " integer, "
            + COLUMN_NUMBER + " text, "
            + COLUMN_DATE + " text, "
            + COLUMN_URL + " text, "
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
