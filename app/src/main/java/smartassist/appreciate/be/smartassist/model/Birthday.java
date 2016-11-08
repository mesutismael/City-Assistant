package smartassist.appreciate.be.smartassist.model;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import smartassist.appreciate.be.smartassist.database.CalendarBirthdayTable;

/**
 * Created by Inneke De Clippel on 2/08/2016.
 */
public class Birthday
{
    private String firstName;
    private String lastName;
    private int year;

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public int getYear()
    {
        return year;
    }

    public static List<Birthday> constructListFromCursor(Cursor cursor)
    {
        List<Birthday> birthdays = new ArrayList<>();

        if(cursor != null && cursor.moveToFirst())
        {
            do
            {
                birthdays.add(Birthday.constructFromCursor(cursor));
            }
            while (cursor.moveToNext());
        }

        return birthdays;
    }

    public static Birthday constructFromCursor(Cursor cursor)
    {
        Birthday birthday = new Birthday();

        birthday.firstName = cursor.getString(cursor.getColumnIndex(CalendarBirthdayTable.COLUMN_FIRST_NAME));
        birthday.lastName = cursor.getString(cursor.getColumnIndex(CalendarBirthdayTable.COLUMN_LAST_NAME));
        birthday.year = cursor.getInt(cursor.getColumnIndex(CalendarBirthdayTable.COLUMN_YEAR));

        return birthday;
    }
}
