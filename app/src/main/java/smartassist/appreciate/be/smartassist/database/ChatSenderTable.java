package smartassist.appreciate.be.smartassist.database;

import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Inneke De Clippel on 8/09/2016.
 */
public class ChatSenderTable
{
    public static final String TABLE_NAME = "chat_senders";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CONVERSATION_ID = "conversation_id";
    public static final String COLUMN_CONTACT_ID = "contact_id";
    public static final String COLUMN_CONTACT_NAME = "contact_name";
    public static final String COLUMN_CONTACT_PHOTO = "contact_photo";
    public static final String COLUMN_JOIN_DATE = "join_date";
    public static final String COLUMN_LEAVE_DATE = "leave_date";

    public static final String COLUMN_ID_FULL = TABLE_NAME + "_" + COLUMN_ID;
    public static final String COLUMN_CONVERSATION_ID_FULL = TABLE_NAME + "_" + COLUMN_CONVERSATION_ID;
    public static final String COLUMN_CONTACT_ID_FULL = TABLE_NAME + "_" + COLUMN_CONTACT_ID;
    public static final String COLUMN_CONTACT_NAME_FULL = TABLE_NAME + "_" + COLUMN_CONTACT_NAME;
    public static final String COLUMN_CONTACT_PHOTO_FULL = TABLE_NAME + "_" + COLUMN_CONTACT_PHOTO;
    public static final String COLUMN_JOIN_DATE_FULL = TABLE_NAME + "_" + COLUMN_JOIN_DATE;
    public static final String COLUMN_LEAVE_DATE_FULL = TABLE_NAME + "_" + COLUMN_LEAVE_DATE;

    public static final Map<String, String> PROJECTION_MAP;

    static
    {
        PROJECTION_MAP = new HashMap<>();
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_ID, TABLE_NAME + "." + COLUMN_ID + " AS " + COLUMN_ID_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_CONVERSATION_ID, TABLE_NAME + "." + COLUMN_CONVERSATION_ID + " AS " + COLUMN_CONVERSATION_ID_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_CONTACT_ID, TABLE_NAME + "." + COLUMN_CONTACT_ID + " AS " + COLUMN_CONTACT_ID_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_CONTACT_NAME, TABLE_NAME + "." + COLUMN_CONTACT_NAME + " AS " + COLUMN_CONTACT_NAME_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_CONTACT_PHOTO, TABLE_NAME + "." + COLUMN_CONTACT_PHOTO + " AS " + COLUMN_CONTACT_PHOTO_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_JOIN_DATE, TABLE_NAME + "." + COLUMN_JOIN_DATE + " AS " + COLUMN_JOIN_DATE_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_LEAVE_DATE, TABLE_NAME + "." + COLUMN_LEAVE_DATE + " AS " + COLUMN_LEAVE_DATE_FULL);
    }

    private static final String CREATE_TABLE = "create table IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_CONVERSATION_ID + " integer, "
            + COLUMN_CONTACT_ID + " integer, "
            + COLUMN_CONTACT_NAME + " text, "
            + COLUMN_CONTACT_PHOTO + " text, "
            + COLUMN_JOIN_DATE + " integer, "
            + COLUMN_LEAVE_DATE + " integer"
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
                    //New table
                    database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
                    onCreate(database);
                    break;
            }

            oldVersion++;
        }
    }
}
