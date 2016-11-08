package smartassist.appreciate.be.smartassist.database;

import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Inneke De Clippel on 8/09/2016.
 */
public class ChatMessageTable
{
    public static final String TABLE_NAME = "chat_messages";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_MESSAGE_ID = "message_id";
    public static final String COLUMN_CONTACT_ID = "sender_id";
    public static final String COLUMN_CONVERSATION_ID = "conversation_id";
    public static final String COLUMN_MESSAGE = "message";
    public static final String COLUMN_SENT_AT = "sent_at";
    public static final String COLUMN_READ = "read";
    public static final String COLUMN_STATE = "state";

    public static final String COLUMN_ID_FULL = TABLE_NAME + "_" + COLUMN_ID;
    public static final String COLUMN_MESSAGE_ID_FULL = TABLE_NAME + "_" + COLUMN_MESSAGE_ID;
    public static final String COLUMN_CONTACT_ID_FULL = TABLE_NAME + "_" + COLUMN_CONTACT_ID;
    public static final String COLUMN_CONVERSATION_ID_FULL = TABLE_NAME + "_" + COLUMN_CONVERSATION_ID;
    public static final String COLUMN_MESSAGE_FULL = TABLE_NAME + "_" + COLUMN_MESSAGE;
    public static final String COLUMN_SENT_AT_FULL = TABLE_NAME + "_" + COLUMN_SENT_AT;
    public static final String COLUMN_READ_FULL = TABLE_NAME + "_" + COLUMN_READ;
    public static final String COLUMN_STATE_FULL = TABLE_NAME + "_" + COLUMN_STATE;

    public static final Map<String, String> PROJECTION_MAP;

    static
    {
        PROJECTION_MAP = new HashMap<>();
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_ID, TABLE_NAME + "." + COLUMN_ID + " AS " + COLUMN_ID_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_MESSAGE_ID, TABLE_NAME + "." + COLUMN_MESSAGE_ID + " AS " + COLUMN_MESSAGE_ID_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_CONTACT_ID, TABLE_NAME + "." + COLUMN_CONTACT_ID + " AS " + COLUMN_CONTACT_ID_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_CONVERSATION_ID, TABLE_NAME + "." + COLUMN_CONVERSATION_ID + " AS " + COLUMN_CONVERSATION_ID_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_MESSAGE, TABLE_NAME + "." + COLUMN_MESSAGE + " AS " + COLUMN_MESSAGE_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_SENT_AT, TABLE_NAME + "." + COLUMN_SENT_AT + " AS " + COLUMN_SENT_AT_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_READ, TABLE_NAME + "." + COLUMN_READ + " AS " + COLUMN_READ_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_STATE, TABLE_NAME + "." + COLUMN_STATE + " AS " + COLUMN_STATE_FULL);
    }

    private static final String CREATE_TABLE = "create table IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_MESSAGE_ID + " integer, "
            + COLUMN_CONTACT_ID + " integer, "
            + COLUMN_CONVERSATION_ID + " integer, "
            + COLUMN_MESSAGE + " text, "
            + COLUMN_SENT_AT + " integer, "
            + COLUMN_READ + " integer, "
            + COLUMN_STATE + " integer"
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
