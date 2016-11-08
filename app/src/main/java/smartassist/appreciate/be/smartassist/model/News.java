package smartassist.appreciate.be.smartassist.model;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import smartassist.appreciate.be.smartassist.database.NewsTable;

/**
 * Created by Inneke De Clippel on 5/08/2016.
 */
public class News
{
    private int id;
    private String title;
    private String body;
    private long startDate;
    private long endDate;
    private double randomPhoto;
    private CategoryPhoto categoryPhoto;

    public int getId()
    {
        return id;
    }

    public String getTitle()
    {
        return title;
    }

    public String getBody()
    {
        return body;
    }

    public long getStartDate()
    {
        return startDate;
    }

    public long getEndDate()
    {
        return endDate;
    }

    public double getRandomPhoto()
    {
        return randomPhoto;
    }

    public CategoryPhoto getCategoryPhoto()
    {
        return categoryPhoto;
    }

    public static List<News> constructListFromCursor(Cursor cursor)
    {
        List<News> news = new ArrayList<>();

        if(cursor != null && cursor.moveToFirst())
        {
            do
            {
                news.add(News.constructFromCursor(cursor));
            }
            while (cursor.moveToNext());
        }

        return news;
    }

    public static News constructFromCursor(Cursor cursor)
    {
        News news = new News();

        news.id = cursor.getInt(cursor.getColumnIndex(NewsTable.COLUMN_NEWS_ID_FULL));
        news.title = cursor.getString(cursor.getColumnIndex(NewsTable.COLUMN_TITLE_FULL));
        news.body = cursor.getString(cursor.getColumnIndex(NewsTable.COLUMN_BODY_FULL));
        news.startDate = cursor.getLong(cursor.getColumnIndex(NewsTable.COLUMN_START_DATE_FULL));
        news.endDate = cursor.getLong(cursor.getColumnIndex(NewsTable.COLUMN_END_DATE_FULL));
        news.randomPhoto = cursor.getDouble(cursor.getColumnIndex(NewsTable.COLUMN_RANDOM_PHOTO_FULL));
        news.categoryPhoto = CategoryPhoto.constructFromCursor(cursor, CategoryPhoto.TYPE_NEWS);

        return news;
    }
}
