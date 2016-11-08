package smartassist.appreciate.be.smartassist.database;

import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Banada ismael on 10/31/2016.
 */

public class ActivityTable
{
    public static final String TABLE_NAME = "activities";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ACTIVITY_TYPE_ID = "activity_type_id";
    public static final String COLUMN_RESIDENCE_ID = "reference_id";
    public static final String COLUMN_PHOTO = "photo";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_START_DATE = "start_date";
    public static final String COLUMN_END_DATE = "end_date";
    public static final String COLUMN_START_TIME = "start_time";
    public static final String COLUMN_END_TIME = "end_time";
    public static final String COLUMN_INVOICE_CODE = "invoice_code";
    public static final String COLUMN_INVOICE_DESCRIPTION = "invoice_description";
    public static final String COLUMN_INVOICE_VAT = "invoice_vat";
    public static final String COLUMN_INVOICE_PRICE = "invoice_price";
    public static final String COLUMN_INVOICE_DATE = "invoice_date";
    public static final String COLUMN_INVOICED = "invoiced";
    public static final String COLUMN_REPEAT_CODE = "repeat_code";
    public static final String COLUMN_REPEAT_AMOUNT = "repeat_amount";
    public static final String COLUMN_REPEAT_END_DATE = "repeat_end_date";
    public static final String COLUMN_AGENT_ITEM_ID = "agent_item_id";
    public static final String COLUMN_NEWS_ITEM_ID = "news_item_id";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_UPDATED_AT = "updated_at";
    public static final String COLUMN_DELETED_AT = "deleted_at";

    public static final String COLUMN_ID_FULL = TABLE_NAME + "_" + COLUMN_ID;
    public static final String COLUMN_ACTIVITY_TYPE_ID_FULL = TABLE_NAME + "_" + COLUMN_ACTIVITY_TYPE_ID;
    public static final String COLUMN_RESIDENCE_ID_FULL = TABLE_NAME + "_" + COLUMN_RESIDENCE_ID;
    public static final String COLUMN_PHOTO_FULL = TABLE_NAME + "_" + COLUMN_PHOTO;
    public static final String COLUMN_NAME_FULL = TABLE_NAME + "_" + COLUMN_NAME;
    public static final String COLUMN_DESCRIPTION_FULL = TABLE_NAME + "_" + COLUMN_DESCRIPTION;
    public static final String COLUMN_LOCATION_FULL = TABLE_NAME + "_" + COLUMN_LOCATION;
    public static final String COLUMN_START_DATE_FULL = TABLE_NAME + "_" + COLUMN_START_DATE;
    public static final String COLUMN_END_DATE_FULL = TABLE_NAME + "_" + COLUMN_END_DATE;
    public static final String COLUMN_START_TIME_FULL = TABLE_NAME + "_" + COLUMN_START_TIME;
    public static final String COLUMN_END_TIME_FULL = TABLE_NAME + "_" + COLUMN_END_TIME;
    public static final String COLUMN_INVOICE_CODE_FULL = TABLE_NAME + "_" + COLUMN_INVOICE_CODE;
    public static final String COLUMN_INVOICE_DESCRIPTION_FULL = TABLE_NAME + "_" + COLUMN_INVOICE_DESCRIPTION;
    public static final String COLUMN_INVOICE_VAT_FULL = TABLE_NAME + "_" + COLUMN_INVOICE_VAT;
    public static final String COLUMN_INVOICE_PRICE_FULL = TABLE_NAME + "_" + COLUMN_INVOICE_PRICE;
    public static final String COLUMN_INVOICE_DATE_FULL = TABLE_NAME + "_" + COLUMN_INVOICE_DATE;
    public static final String COLUMN_INVOICED_FULL = TABLE_NAME + "_" + COLUMN_INVOICED;
    public static final String COLUMN_REPEAT_CODE_FULL = TABLE_NAME + "_" + COLUMN_REPEAT_CODE;
    public static final String COLUMN_REPEAT_AMOUNT_FULL = TABLE_NAME + "_" + COLUMN_REPEAT_AMOUNT;
    public static final String COLUMN_REPEAT_END_DATE_FULL = TABLE_NAME + "_" + COLUMN_REPEAT_END_DATE;
    public static final String COLUMN_AGENT_ITEM_ID_FULL = TABLE_NAME + "_" + COLUMN_AGENT_ITEM_ID;
    public static final String COLUMN_NEWS_ITEM_ID_FULL = TABLE_NAME + "_" + COLUMN_NEWS_ITEM_ID;
    public static final String COLUMN_CREATED_AT_FULL = TABLE_NAME + "_" + COLUMN_CREATED_AT;
    public static final String COLUMN_UPDATED_AT_FULL = TABLE_NAME + "_" + COLUMN_UPDATED_AT;
    public static final String COLUMN_DELETED_AT_FULL = TABLE_NAME + "_" + COLUMN_DELETED_AT;



    public static final Map<String, String> PROJECTION_MAP;
    private static final String CREATE_TABLE = "create table IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_ACTIVITY_TYPE_ID + " integer, "
            + COLUMN_RESIDENCE_ID + " integer, "
            + COLUMN_PHOTO + " text, "
            + COLUMN_NAME + " text, "
            + COLUMN_DESCRIPTION + " text, "
            + COLUMN_LOCATION + " text, "
            + COLUMN_START_DATE + " long, "
            + COLUMN_END_DATE + " long, "
            + COLUMN_START_TIME + " text, "
            + COLUMN_END_TIME + " text, "
            + COLUMN_INVOICE_CODE + " integer, "
            + COLUMN_INVOICE_DESCRIPTION + " text, "
            + COLUMN_INVOICE_PRICE + " integer, "
            + COLUMN_INVOICE_VAT + " text, "
            + COLUMN_INVOICED + " text, "
            + COLUMN_INVOICE_DATE + " text, "
            + COLUMN_REPEAT_CODE + " integer, "
            + COLUMN_REPEAT_AMOUNT + " integer, "
            + COLUMN_REPEAT_END_DATE + " long, "
            + COLUMN_AGENT_ITEM_ID + " integer, "
            + COLUMN_NEWS_ITEM_ID + " integer, "
            + COLUMN_CREATED_AT + " long, "
            + COLUMN_UPDATED_AT + " long, "
            + COLUMN_DELETED_AT + " long "
            + ");";

    static
    {
        PROJECTION_MAP = new HashMap<>();
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_ID, TABLE_NAME + "." + COLUMN_ID + " AS " + COLUMN_ID_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_ACTIVITY_TYPE_ID, TABLE_NAME + "." + COLUMN_ACTIVITY_TYPE_ID + " AS " + COLUMN_ACTIVITY_TYPE_ID_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_RESIDENCE_ID, TABLE_NAME + "." + COLUMN_RESIDENCE_ID + " AS " + COLUMN_RESIDENCE_ID_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_PHOTO, TABLE_NAME + "." + COLUMN_PHOTO + " AS " + COLUMN_PHOTO_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_NAME, TABLE_NAME + "." + COLUMN_NAME + " AS " + COLUMN_NAME_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_DESCRIPTION, TABLE_NAME + "." + COLUMN_DESCRIPTION + " AS " + COLUMN_DESCRIPTION_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_LOCATION, TABLE_NAME + "." + COLUMN_LOCATION + " AS " + COLUMN_LOCATION_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_START_DATE, TABLE_NAME + "." + COLUMN_START_DATE + " AS " + COLUMN_START_DATE_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_END_DATE, TABLE_NAME + "." + COLUMN_END_DATE + " AS " + COLUMN_END_DATE_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_START_TIME, TABLE_NAME + "." + COLUMN_START_TIME + " AS " + COLUMN_START_TIME_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_END_TIME, TABLE_NAME + "." + COLUMN_END_TIME + " AS " + COLUMN_END_TIME_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_INVOICE_CODE, TABLE_NAME + "." + COLUMN_INVOICE_CODE + " AS " + COLUMN_INVOICE_CODE_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_INVOICE_DESCRIPTION, TABLE_NAME + "." + COLUMN_INVOICE_DESCRIPTION + " AS " + COLUMN_INVOICE_DESCRIPTION_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_INVOICE_VAT, TABLE_NAME + "." + COLUMN_INVOICE_VAT + " AS " + COLUMN_INVOICE_VAT_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_INVOICE_PRICE, TABLE_NAME + "." + COLUMN_INVOICE_PRICE + " AS " + COLUMN_INVOICE_PRICE_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_INVOICE_DATE, TABLE_NAME + "." + COLUMN_INVOICE_DATE + " AS " + COLUMN_INVOICE_DATE_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_INVOICED, TABLE_NAME + "." + COLUMN_INVOICED + " AS " + COLUMN_INVOICED_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_REPEAT_CODE, TABLE_NAME + "." + COLUMN_REPEAT_CODE + " AS " + COLUMN_REPEAT_CODE_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_REPEAT_AMOUNT, TABLE_NAME + "." + COLUMN_REPEAT_AMOUNT + " AS " + COLUMN_REPEAT_AMOUNT_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_REPEAT_END_DATE, TABLE_NAME + "." + COLUMN_REPEAT_END_DATE + " AS " + COLUMN_REPEAT_END_DATE_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_AGENT_ITEM_ID, TABLE_NAME + "." + COLUMN_AGENT_ITEM_ID + " AS " + COLUMN_AGENT_ITEM_ID_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_NEWS_ITEM_ID, TABLE_NAME + "." + COLUMN_NEWS_ITEM_ID + " AS " + COLUMN_NEWS_ITEM_ID_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_CREATED_AT, TABLE_NAME + "." + COLUMN_CREATED_AT + " AS " + COLUMN_CREATED_AT_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_UPDATED_AT, TABLE_NAME + "." + COLUMN_UPDATED_AT + " AS " + COLUMN_UPDATED_AT_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_DELETED_AT, TABLE_NAME + "." + COLUMN_DELETED_AT + " AS " + COLUMN_DELETED_AT_FULL);


    }

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

