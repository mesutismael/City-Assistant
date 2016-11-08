package smartassist.appreciate.be.smartassist.database;

import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by banada ismael on 10/31/2016.
 */

public class ActivityTypesTable {
    public static final String TABLE_NAME = "activity_types";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static  final String COLUMN_CREATION_DATE="created_at";
    public static  final String COLUMN_UPDATE_DATE="updated_at";
    public static final String COLUMN_DELETION_DATE = "deleted_at";
    public static final String COLUMN_IMAGE_ID = "image_id";
    public static final String COLUMN_LANDSCAPE_IMAGE = "landscape_image";
    public static final String COLUMN_POTRAIT_IMAGE = "potrait_image";


    public static final String COLUMN_ID_FULL = TABLE_NAME + "_" + COLUMN_ID;
    public static final String COLUMN_NAME_FULL = TABLE_NAME + "_" + COLUMN_NAME;
    public static final String COLUMN_CREATION_DATE_FULL = TABLE_NAME + "_" + COLUMN_CREATION_DATE;
    public static final String COLUMN_UPDATE_DATE_FULL = TABLE_NAME + "_" + COLUMN_UPDATE_DATE;
    public static final String COLUMN_DELETION_DATE_FULL = TABLE_NAME + "_" + COLUMN_DELETION_DATE;
    public static final String COLUMN_IMAGE_ID_FULL = TABLE_NAME + "_" + COLUMN_IMAGE_ID;
    public static final String COLUMN_LANDSCAPE_IMAGE_FULL = TABLE_NAME + "_" + COLUMN_LANDSCAPE_IMAGE;
    public static final String COLUMN_POTRAIT_IMAGE_FULL = TABLE_NAME + "_" + COLUMN_POTRAIT_IMAGE;



    public static final Map<String, String> PROJECTION_MAP;
    private static final String CREATE_TABLE = "create table IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text, "
            + COLUMN_CREATION_DATE + " long, "
            + COLUMN_UPDATE_DATE + " long, "
            + COLUMN_DELETION_DATE + " long, "
            + COLUMN_IMAGE_ID+ " integer , "
            + COLUMN_LANDSCAPE_IMAGE + " text, "
            + COLUMN_POTRAIT_IMAGE + " text "
            + ");";

    static
    {
        PROJECTION_MAP = new HashMap<>();
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_ID, TABLE_NAME + "." + COLUMN_ID + " AS " + COLUMN_ID_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_NAME, TABLE_NAME + "." + COLUMN_NAME + " AS " + COLUMN_NAME_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_CREATION_DATE, TABLE_NAME + "." + COLUMN_CREATION_DATE + " AS " + COLUMN_CREATION_DATE_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_UPDATE_DATE, TABLE_NAME + "." + COLUMN_UPDATE_DATE + " AS " + COLUMN_UPDATE_DATE_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_DELETION_DATE, TABLE_NAME + "." + COLUMN_DELETION_DATE + " AS " + COLUMN_DELETION_DATE_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_IMAGE_ID, TABLE_NAME + "." + COLUMN_IMAGE_ID + " AS " + COLUMN_IMAGE_ID_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_LANDSCAPE_IMAGE, TABLE_NAME + "." + COLUMN_LANDSCAPE_IMAGE + " AS " + COLUMN_LANDSCAPE_IMAGE_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_POTRAIT_IMAGE, TABLE_NAME + "." + COLUMN_POTRAIT_IMAGE + " AS " + COLUMN_POTRAIT_IMAGE_FULL);

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
