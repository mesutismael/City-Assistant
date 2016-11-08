package smartassist.appreciate.be.smartassist.model;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import smartassist.appreciate.be.smartassist.database.CareBookTable;

/**
 * Created by Inneke De Clippel on 2/08/2016.
 */
public class CarebookItem
{
    private String message;
    private String author;
    private long date;
    private int smiley;

    public String getMessage()
    {
        return message;
    }

    public String getAuthor()
    {
        return author;
    }

    public long getDate()
    {
        return date;
    }

    public int getSmiley()
    {
        return smiley;
    }

    public static List<CarebookItem> constructListFromCursor(Cursor cursor)
    {
        List<CarebookItem> carebookItems = new ArrayList<>();

        if(cursor != null && cursor.moveToFirst())
        {
            do
            {
                carebookItems.add(CarebookItem.constructFromCursor(cursor));
            }
            while (cursor.moveToNext());
        }

        return carebookItems;
    }

    public static CarebookItem constructFromCursor(Cursor cursor)
    {
        CarebookItem carebookItem = new CarebookItem();

        carebookItem.message = cursor.getString(cursor.getColumnIndex(CareBookTable.COLUMN_MESSAGE));
        carebookItem.author = cursor.getString(cursor.getColumnIndex(CareBookTable.COLUMN_AUTHOR));
        carebookItem.date = cursor.getLong(cursor.getColumnIndex(CareBookTable.COLUMN_CREATED_AT));
        carebookItem.smiley = cursor.getInt(cursor.getColumnIndex(CareBookTable.COLUMN_SMILEY));

        return carebookItem;
    }
}
