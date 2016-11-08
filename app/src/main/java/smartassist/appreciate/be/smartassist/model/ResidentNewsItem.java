package smartassist.appreciate.be.smartassist.model;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import smartassist.appreciate.be.smartassist.database.ResidentNewsTable;

/**
 * Created by Banada ismael on 25/10/2016.
 */
public class ResidentNewsItem
{
    private int id;
    private int residence_id;
    private String title;
    private String body;
    private String file;
    private int hard_reminder;
    private long createDate;
    private long updateDate;

    public static List<ResidentNewsItem> constructListFromCursor(Cursor cursor)
    {
        List<ResidentNewsItem> news = new ArrayList<>();

        if(cursor != null && cursor.moveToFirst())
        {
            do
            {
                news.add(ResidentNewsItem.constructFromCursor(cursor));
            }
            while (cursor.moveToNext());
        }


        return news;
    }

    public static ResidentNewsItem constructFromCursor(Cursor cursor)
    {
        ResidentNewsItem residentNewsItem = new ResidentNewsItem();
        residentNewsItem.id = cursor.getInt(cursor.getColumnIndex(ResidentNewsTable.COLUMN_ID));
        residentNewsItem.residence_id = cursor.getInt(cursor.getColumnIndex(ResidentNewsTable.COLUMN_RESIDENCE_ID));
        residentNewsItem.title = cursor.getString(cursor.getColumnIndex(ResidentNewsTable.COLUMN_TITLE));
        residentNewsItem.body = cursor.getString(cursor.getColumnIndex(ResidentNewsTable.COLUMN_BODY));
        residentNewsItem.createDate = cursor.getLong(cursor.getColumnIndex(ResidentNewsTable.COLUMN_CREATION_DATE));
        residentNewsItem.updateDate = cursor.getLong(cursor.getColumnIndex(ResidentNewsTable.COLUMN_LAST_UPDATE));
        residentNewsItem.file = cursor.getString(cursor.getColumnIndex(ResidentNewsTable.COLUMN_FILE));
        residentNewsItem.hard_reminder = cursor.getInt(cursor.getColumnIndex(ResidentNewsTable.COLUMN_HARD_REMINDER));

        return residentNewsItem;
    }

    public int getId() {return id;}

    public int getResidence_id() {return residence_id;}

    public String getBody() {
        return body;
    }

    public String getTitle() {
        return title;
    }

    public String getFile() {
        return file;
    }

    public int getHard_reminder() {
        return hard_reminder;
    }

    public long getCreateDate() {
        return createDate;
    }

    public long getUpdateDate() {
        return updateDate;
    }
}
