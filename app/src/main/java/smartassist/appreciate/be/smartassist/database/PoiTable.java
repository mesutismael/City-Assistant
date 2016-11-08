package smartassist.appreciate.be.smartassist.database;

import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Map;

import smartassist.appreciate.be.smartassist.model.api.PoiItem;

/**
 * Created by Inneke on 10/02/2015.
 */
public class PoiTable
{
    public static final String TABLE_NAME = "poi";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_POI_ID = "poi_id";
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_STREET = "street";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_POSTAL_CODE = "postal_code";
    public static final String COLUMN_LOCALITY = "locality";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_PHONE = "phone_number";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_DISTANCE = "distance";
    public static final String COLUMN_RANDOM_PHOTO = "random_photo";
    public static final String COLUMN_FAVORITE = "favorite";
    public static final String COLUMN_LAST_UPDATE = "last_update";
    public static final String COLUMN_OPENING_TIMES = "opening_times";

    public static final String COLUMN_ID_FULL = TABLE_NAME + "_" + COLUMN_ID;
    public static final String COLUMN_POI_ID_FULL = TABLE_NAME + "_" + COLUMN_POI_ID;
    public static final String COLUMN_CATEGORY_ID_FULL = TABLE_NAME + "_" + COLUMN_CATEGORY_ID;
    public static final String COLUMN_IMAGE_FULL = TABLE_NAME + "_" + COLUMN_IMAGE;
    public static final String COLUMN_NAME_FULL = TABLE_NAME + "_" + COLUMN_NAME;
    public static final String COLUMN_DESCRIPTION_FULL = TABLE_NAME + "_" + COLUMN_DESCRIPTION;
    public static final String COLUMN_STREET_FULL = TABLE_NAME + "_" + COLUMN_STREET;
    public static final String COLUMN_NUMBER_FULL = TABLE_NAME + "_" + COLUMN_NUMBER;
    public static final String COLUMN_POSTAL_CODE_FULL = TABLE_NAME + "_" + COLUMN_POSTAL_CODE;
    public static final String COLUMN_LOCALITY_FULL = TABLE_NAME + "_" + COLUMN_LOCALITY;
    public static final String COLUMN_LATITUDE_FULL = TABLE_NAME + "_" + COLUMN_LATITUDE;
    public static final String COLUMN_LONGITUDE_FULL = TABLE_NAME + "_" + COLUMN_LONGITUDE;
    public static final String COLUMN_PHONE_FULL = TABLE_NAME + "_" + COLUMN_PHONE;
    public static final String COLUMN_EMAIL_FULL = TABLE_NAME + "_" + COLUMN_EMAIL;
    public static final String COLUMN_DISTANCE_FULL = TABLE_NAME + "_" + COLUMN_DISTANCE;
    public static final String COLUMN_RANDOM_PHOTO_FULL = TABLE_NAME + "_" + COLUMN_RANDOM_PHOTO;
    public static final String COLUMN_FAVORITE_FULL = TABLE_NAME + "_" + COLUMN_FAVORITE;
    public static final String COLUMN_LAST_UPDATE_FULL = TABLE_NAME + "_" + COLUMN_LAST_UPDATE;

    public static final Map<String, String> PROJECTION_MAP;

    static
    {
        PROJECTION_MAP = new HashMap<>();
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_ID, TABLE_NAME + "." + COLUMN_ID + " AS " + COLUMN_ID_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_POI_ID, TABLE_NAME + "." + COLUMN_POI_ID + " AS " + COLUMN_POI_ID_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_CATEGORY_ID, TABLE_NAME + "." + COLUMN_CATEGORY_ID + " AS " + COLUMN_CATEGORY_ID_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_IMAGE, TABLE_NAME + "." + COLUMN_IMAGE + " AS " + COLUMN_IMAGE_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_NAME, TABLE_NAME + "." + COLUMN_NAME + " AS " + COLUMN_NAME_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_DESCRIPTION, TABLE_NAME + "." + COLUMN_DESCRIPTION + " AS " + COLUMN_DESCRIPTION_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_STREET, TABLE_NAME + "." + COLUMN_STREET + " AS " + COLUMN_STREET_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_NUMBER, TABLE_NAME + "." + COLUMN_NUMBER + " AS " + COLUMN_NUMBER_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_POSTAL_CODE, TABLE_NAME + "." + COLUMN_POSTAL_CODE + " AS " + COLUMN_POSTAL_CODE_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_LOCALITY, TABLE_NAME + "." + COLUMN_LOCALITY + " AS " + COLUMN_LOCALITY_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_LATITUDE, TABLE_NAME + "." + COLUMN_LATITUDE + " AS " + COLUMN_LATITUDE_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_LONGITUDE, TABLE_NAME + "." + COLUMN_LONGITUDE + " AS " + COLUMN_LONGITUDE_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_PHONE, TABLE_NAME + "." + COLUMN_PHONE + " AS " + COLUMN_PHONE_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_EMAIL, TABLE_NAME + "." + COLUMN_EMAIL + " AS " + COLUMN_EMAIL_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_DISTANCE, TABLE_NAME + "." + COLUMN_DISTANCE + " AS " + COLUMN_DISTANCE_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_RANDOM_PHOTO, TABLE_NAME + "." + COLUMN_RANDOM_PHOTO + " AS " + COLUMN_RANDOM_PHOTO_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_FAVORITE, TABLE_NAME + "." + COLUMN_FAVORITE + " AS " + COLUMN_FAVORITE_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_LAST_UPDATE, TABLE_NAME + "." + COLUMN_LAST_UPDATE + " AS " + COLUMN_LAST_UPDATE_FULL);
    }

    private static final String CREATE_TABLE = "create table IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_POI_ID + " integer, "
            + COLUMN_CATEGORY_ID + " integer, "
            + COLUMN_IMAGE + " TEXT, "
            + COLUMN_NAME + " text, "
            + COLUMN_DESCRIPTION + " text, "
            + COLUMN_STREET + " text, "
            + COLUMN_NUMBER + " text, "
            + COLUMN_POSTAL_CODE + " text, "
            + COLUMN_LOCALITY + " text, "
            + COLUMN_LATITUDE + " real, "
            + COLUMN_LONGITUDE + " real, "
            + COLUMN_PHONE + " text,"
            + COLUMN_EMAIL + " text,"
            + COLUMN_DISTANCE + " real, "
            + COLUMN_RANDOM_PHOTO + " real, "
            + COLUMN_FAVORITE+ " integer, "
            + COLUMN_LAST_UPDATE + " long,"
            + COLUMN_OPENING_TIMES+ " integer"
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
