package smartassist.appreciate.be.smartassist.database;

import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Inneke on 10/02/2015.
 */
public class PoiCategoryTable
{
    public static final String TABLE_NAME = "poi_categories";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_IMAGE_PORTRAIT_1 = "image_portrait_1";
    public static final String COLUMN_IMAGE_PORTRAIT_2 = "image_portrait_2";
    public static final String COLUMN_IMAGE_PORTRAIT_3 = "image_portrait_3";
    public static final String COLUMN_IMAGE_PORTRAIT_4 = "image_portrait_4";
    public static final String COLUMN_IMAGE_PORTRAIT_5 = "image_portrait_5";
    public static final String COLUMN_IMAGE_LANDSCAPE_1 = "image_landscape_1";
    public static final String COLUMN_IMAGE_LANDSCAPE_2 = "image_landscape_2";
    public static final String COLUMN_IMAGE_LANDSCAPE_3 = "image_landscape_3";
    public static final String COLUMN_IMAGE_LANDSCAPE_4 = "image_landscape_4";
    public static final String COLUMN_IMAGE_LANDSCAPE_5 = "image_landscape_5";
    public static final String COLUMN_IS_MAIN = "is_main_category";
    public static final String COLUMN_LAST_UPDATE = "last_update";

    public static final String COLUMN_ID_FULL = TABLE_NAME + "_" + COLUMN_ID;
    public static final String COLUMN_CATEGORY_ID_FULL = TABLE_NAME + "_" + COLUMN_CATEGORY_ID;
    public static final String COLUMN_NAME_FULL = TABLE_NAME + "_" + COLUMN_NAME;
    public static final String COLUMN_IMAGE_PORTRAIT_1_FULL = TABLE_NAME + "_" + COLUMN_IMAGE_PORTRAIT_1;
    public static final String COLUMN_IMAGE_PORTRAIT_2_FULL = TABLE_NAME + "_" + COLUMN_IMAGE_PORTRAIT_2;
    public static final String COLUMN_IMAGE_PORTRAIT_3_FULL = TABLE_NAME + "_" + COLUMN_IMAGE_PORTRAIT_3;
    public static final String COLUMN_IMAGE_PORTRAIT_4_FULL = TABLE_NAME + "_" + COLUMN_IMAGE_PORTRAIT_4;
    public static final String COLUMN_IMAGE_PORTRAIT_5_FULL = TABLE_NAME + "_" + COLUMN_IMAGE_PORTRAIT_5;
    public static final String COLUMN_IMAGE_LANDSCAPE_1_FULL = TABLE_NAME + "_" + COLUMN_IMAGE_LANDSCAPE_1;
    public static final String COLUMN_IMAGE_LANDSCAPE_2_FULL = TABLE_NAME + "_" + COLUMN_IMAGE_LANDSCAPE_2;
    public static final String COLUMN_IMAGE_LANDSCAPE_3_FULL = TABLE_NAME + "_" + COLUMN_IMAGE_LANDSCAPE_3;
    public static final String COLUMN_IMAGE_LANDSCAPE_4_FULL = TABLE_NAME + "_" + COLUMN_IMAGE_LANDSCAPE_4;
    public static final String COLUMN_IMAGE_LANDSCAPE_5_FULL = TABLE_NAME + "_" + COLUMN_IMAGE_LANDSCAPE_5;
    public static final String COLUMN_IS_MAIN_FULL = TABLE_NAME + "_" + COLUMN_IS_MAIN;
    public static final String COLUMN_LAST_UPDATE_FULL = TABLE_NAME + "_" + COLUMN_LAST_UPDATE;

    public static final Map<String, String> PROJECTION_MAP;

    static
    {
        PROJECTION_MAP = new HashMap<>();
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_ID, TABLE_NAME + "." + COLUMN_ID + " AS " + COLUMN_ID_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_CATEGORY_ID, TABLE_NAME + "." + COLUMN_CATEGORY_ID + " AS " + COLUMN_CATEGORY_ID_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_NAME, TABLE_NAME + "." + COLUMN_NAME + " AS " + COLUMN_NAME_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_IMAGE_PORTRAIT_1, TABLE_NAME + "." + COLUMN_IMAGE_PORTRAIT_1 + " AS " + COLUMN_IMAGE_PORTRAIT_1_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_IMAGE_PORTRAIT_2, TABLE_NAME + "." + COLUMN_IMAGE_PORTRAIT_2 + " AS " + COLUMN_IMAGE_PORTRAIT_2_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_IMAGE_PORTRAIT_3, TABLE_NAME + "." + COLUMN_IMAGE_PORTRAIT_3 + " AS " + COLUMN_IMAGE_PORTRAIT_3_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_IMAGE_PORTRAIT_4, TABLE_NAME + "." + COLUMN_IMAGE_PORTRAIT_4 + " AS " + COLUMN_IMAGE_PORTRAIT_4_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_IMAGE_PORTRAIT_5, TABLE_NAME + "." + COLUMN_IMAGE_PORTRAIT_5 + " AS " + COLUMN_IMAGE_PORTRAIT_5_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_IMAGE_LANDSCAPE_1, TABLE_NAME + "." + COLUMN_IMAGE_LANDSCAPE_1 + " AS " + COLUMN_IMAGE_LANDSCAPE_1_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_IMAGE_LANDSCAPE_2, TABLE_NAME + "." + COLUMN_IMAGE_LANDSCAPE_2 + " AS " + COLUMN_IMAGE_LANDSCAPE_2_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_IMAGE_LANDSCAPE_3, TABLE_NAME + "." + COLUMN_IMAGE_LANDSCAPE_3 + " AS " + COLUMN_IMAGE_LANDSCAPE_3_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_IMAGE_LANDSCAPE_4, TABLE_NAME + "." + COLUMN_IMAGE_LANDSCAPE_4 + " AS " + COLUMN_IMAGE_LANDSCAPE_4_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_IMAGE_LANDSCAPE_5, TABLE_NAME + "." + COLUMN_IMAGE_LANDSCAPE_5 + " AS " + COLUMN_IMAGE_LANDSCAPE_5_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_IS_MAIN, TABLE_NAME + "." + COLUMN_IS_MAIN + " AS " + COLUMN_IS_MAIN_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_LAST_UPDATE, TABLE_NAME + "." + COLUMN_LAST_UPDATE + " AS " + COLUMN_LAST_UPDATE_FULL);
    }

    private static final String CREATE_TABLE = "create table IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_CATEGORY_ID + " integer, "
            + COLUMN_NAME + " text, "
            + COLUMN_IMAGE_PORTRAIT_1 + " text, "
            + COLUMN_IMAGE_PORTRAIT_2 + " text, "
            + COLUMN_IMAGE_PORTRAIT_3 + " text, "
            + COLUMN_IMAGE_PORTRAIT_4 + " text, "
            + COLUMN_IMAGE_PORTRAIT_5 + " text, "
            + COLUMN_IMAGE_LANDSCAPE_1 + " text, "
            + COLUMN_IMAGE_LANDSCAPE_2 + " text, "
            + COLUMN_IMAGE_LANDSCAPE_3 + " text, "
            + COLUMN_IMAGE_LANDSCAPE_4 + " text, "
            + COLUMN_IMAGE_LANDSCAPE_5 + " text, "
            + COLUMN_IS_MAIN + " integer, "
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
