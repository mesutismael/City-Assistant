package smartassist.appreciate.be.smartassist.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Inneke on 30/01/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "smart_assist.db";
    public static final int DATABASE_VERSION = 3;

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        ModuleTable.onCreate(db);
        PoiCategoryTable.onCreate(db);
        PoiTable.onCreate(db);
        OpeningTimesTable.onCreate(db);
        CalendarEventTable.onCreate(db);
        CalendarBirthdayTable.onCreate(db);
        NewsTable.onCreate(db);
        NewsCategoryTable.onCreate(db);
        WeatherTable.onCreate(db);
        RssTable.onCreate(db);
        EmergencyTable.onCreate(db);
        EmergencyCategoryTable.onCreate(db);
        PhotoTable.onCreate(db);
        NotificationTable.onCreate(db);
        CareBookTable.onCreate(db);
        MedicationTable.onCreate(db);
        InvoiceTable.onCreate(db);
        HabitantTable.onCreate(db);
        ChatMessageTable.onCreate(db);
        ChatConversationTable.onCreate(db);
        ChatSenderTable.onCreate(db);
        ResidentNewsTable.onCreate(db);
        ActivityTable.onCreate(db);
        ActivityTypesTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        ModuleTable.onUpgrade(db, oldVersion, newVersion);
        PoiCategoryTable.onUpgrade(db, oldVersion, newVersion);
        PoiTable.onUpgrade(db, oldVersion, newVersion);
        OpeningTimesTable.onUpgrade(db, oldVersion, newVersion);
        CalendarEventTable.onUpgrade(db, oldVersion, newVersion);
        CalendarBirthdayTable.onUpgrade(db, oldVersion, newVersion);
        NewsTable.onUpgrade(db, oldVersion, newVersion);
        NewsCategoryTable.onUpgrade(db, oldVersion, newVersion);
        WeatherTable.onUpgrade(db, oldVersion, newVersion);
        RssTable.onUpgrade(db, oldVersion, newVersion);
        EmergencyTable.onUpgrade(db, oldVersion, newVersion);
        EmergencyCategoryTable.onUpgrade(db, oldVersion, newVersion);
        PhotoTable.onUpgrade(db, oldVersion, newVersion);
        NotificationTable.onUpgrade(db, oldVersion, newVersion);
        CareBookTable.onUpgrade(db, oldVersion, newVersion);
        MedicationTable.onUpgrade(db, oldVersion, newVersion);
        InvoiceTable.onUpgrade(db, oldVersion, newVersion);
        HabitantTable.onUpgrade(db, oldVersion, newVersion);
        ChatMessageTable.onUpgrade(db, oldVersion, newVersion);
        ChatConversationTable.onUpgrade(db, oldVersion, newVersion);
        ChatSenderTable.onUpgrade(db, oldVersion, newVersion);
        ResidentNewsTable.onUpgrade(db, oldVersion, newVersion);
        ActivityTable.onUpgrade(db, oldVersion, newVersion);
        ActivityTypesTable.onUpgrade(db, oldVersion, newVersion);

        while (oldVersion < newVersion)
        {
            switch (oldVersion)
            {
                case 1:
                    //Removed table
                    db.execSQL("DROP TABLE IF EXISTS chat");
                    break;
            }

            oldVersion++;
        }
    }
}
