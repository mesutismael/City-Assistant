package smartassist.appreciate.be.smartassist.model;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import smartassist.appreciate.be.smartassist.database.PhotoTable;

/**
 * Created by Inneke De Clippel on 5/08/2016.
 */
public class Photo
{
    private int id;
    private String image;
    private String thumbnail;

    public int getId()
    {
        return id;
    }

    public String getImage()
    {
        return image;
    }

    public String getThumbnail()
    {
        return thumbnail;
    }

    public static List<Photo> constructListFromCursor(Cursor cursor)
    {
        List<Photo> photos = new ArrayList<>();

        if(cursor != null && cursor.moveToFirst())
        {
            do
            {
                photos.add(Photo.constructFromCursor(cursor));
            }
            while (cursor.moveToNext());
        }

        return photos;
    }

    public static Photo constructFromCursor(Cursor cursor)
    {
        Photo photo = new Photo();

        photo.id = cursor.getInt(cursor.getColumnIndex(PhotoTable.COLUMN_PHOTO_ID));
        photo.image = cursor.getString(cursor.getColumnIndex(PhotoTable.COLUMN_IMAGE));
        photo.thumbnail = cursor.getString(cursor.getColumnIndex(PhotoTable.COLUMN_THUMBNAIL));

        return photo;
    }
}
