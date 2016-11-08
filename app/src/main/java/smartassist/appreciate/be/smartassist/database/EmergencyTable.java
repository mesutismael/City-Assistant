package smartassist.appreciate.be.smartassist.database;

import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Inneke on 26/02/2015.
 */
public class EmergencyTable
{
    public static final String TABLE_NAME = "emergencies";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_EMERGENCY_ID = "emergency_id";
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHOTO = "photo";
    public static final String COLUMN_NUMBER = "phone_number";
    public static final String COLUMN_RANDOM_PHOTO = "random_photo";
    public static final String COLUMN_HASH = "hash";
    public static final String COLUMN_HAS_QR = "has_qr";
    public static final String COLUMN_CAN_CHAT = "can_chat";
    public static final String COLUMN_LAST_UPDATE = "last_update";

    public static final String COLUMN_ID_FULL = TABLE_NAME + "_" + COLUMN_ID;
    public static final String COLUMN_EMERGENCY_ID_FULL = TABLE_NAME + "_" + COLUMN_EMERGENCY_ID;
    public static final String COLUMN_CATEGORY_ID_FULL = TABLE_NAME + "_" + COLUMN_CATEGORY_ID;
    public static final String COLUMN_NAME_FULL = TABLE_NAME + "_" + COLUMN_NAME;
    public static final String COLUMN_PHOTO_FULL = TABLE_NAME + "_" + COLUMN_PHOTO;
    public static final String COLUMN_NUMBER_FULL = TABLE_NAME + "_" + COLUMN_NUMBER;
    public static final String COLUMN_RANDOM_PHOTO_FULL = TABLE_NAME + "_" + COLUMN_RANDOM_PHOTO;
    public static final String COLUMN_HASH_FULL = TABLE_NAME + "_" + COLUMN_HASH;
    public static final String COLUMN_HAS_QR_FULL = TABLE_NAME + "_" + COLUMN_HAS_QR;
    public static final String COLUMN_CAN_CHAT_FULL = TABLE_NAME + "_" + COLUMN_CAN_CHAT;
    public static final String COLUMN_LAST_UPDATE_FULL = TABLE_NAME + "_" + COLUMN_LAST_UPDATE;

    public static final Map<String, String> PROJECTION_MAP;

    static
    {
        PROJECTION_MAP = new HashMap<>();
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_ID, TABLE_NAME + "." + COLUMN_ID + " AS " + COLUMN_ID_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_EMERGENCY_ID, TABLE_NAME + "." + COLUMN_EMERGENCY_ID + " AS " + COLUMN_EMERGENCY_ID_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_CATEGORY_ID, TABLE_NAME + "." + COLUMN_CATEGORY_ID + " AS " + COLUMN_CATEGORY_ID_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_NAME, TABLE_NAME + "." + COLUMN_NAME + " AS " + COLUMN_NAME_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_PHOTO, TABLE_NAME + "." + COLUMN_PHOTO + " AS " + COLUMN_PHOTO_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_NUMBER, TABLE_NAME + "." + COLUMN_NUMBER + " AS " + COLUMN_NUMBER_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_RANDOM_PHOTO, TABLE_NAME + "." + COLUMN_RANDOM_PHOTO + " AS " + COLUMN_RANDOM_PHOTO_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_HASH, TABLE_NAME + "." + COLUMN_HASH + " AS " + COLUMN_HASH_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_HAS_QR, TABLE_NAME + "." + COLUMN_HAS_QR + " AS " + COLUMN_HAS_QR_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_CAN_CHAT, TABLE_NAME + "." + COLUMN_CAN_CHAT + " AS " + COLUMN_CAN_CHAT_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_LAST_UPDATE, TABLE_NAME + "." + COLUMN_LAST_UPDATE + " AS " + COLUMN_LAST_UPDATE_FULL);
    }

    private static final String CREATE_TABLE = "create table IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_EMERGENCY_ID + " integer, "
            + COLUMN_CATEGORY_ID + " integer, "
            + COLUMN_NAME + " text, "
            + COLUMN_PHOTO + " text, "
            + COLUMN_NUMBER + " text, "
            + COLUMN_RANDOM_PHOTO + " real, "
            + COLUMN_HASH + " text, "
            + COLUMN_HAS_QR + " integer, "
            + COLUMN_CAN_CHAT + " integer, "
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
