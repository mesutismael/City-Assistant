package smartassist.appreciate.be.smartassist.model;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import smartassist.appreciate.be.smartassist.database.RssTable;

/**
 * Created by Inneke De Clippel on 5/08/2016.
 */
public class Rss
{
    private int id;
    private String image;
    private String title;
    private String intro;
    private long date;

    public int getId()
    {
        return id;
    }

    public String getImage()
    {
        return image;
    }

    public String getTitle()
    {
        return title;
    }

    public String getIntro()
    {
        return intro;
    }

    public long getDate()
    {
        return date;
    }

    public static List<Rss> constructListFromCursor(Cursor cursor)
    {
        List<Rss> rss = new ArrayList<>();

        if(cursor != null && cursor.moveToFirst())
        {
            do
            {
                rss.add(Rss.constructFromCursor(cursor));
            }
            while (cursor.moveToNext());
        }

        return rss;
    }

    public static Rss constructFromCursor(Cursor cursor)
    {
        Rss rss = new Rss();

        rss.id = cursor.getInt(cursor.getColumnIndex(RssTable.COLUMN_RSS_ID));
        rss.image = cursor.getString(cursor.getColumnIndex(RssTable.COLUMN_IMAGE));
        rss.title = cursor.getString(cursor.getColumnIndex(RssTable.COLUMN_TITLE));
        rss.intro = cursor.getString(cursor.getColumnIndex(RssTable.COLUMN_INTRO));
        rss.date = cursor.getLong(cursor.getColumnIndex(RssTable.COLUMN_DATE));

        return rss;
    }
}
