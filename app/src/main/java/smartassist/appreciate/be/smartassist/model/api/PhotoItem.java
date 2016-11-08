package smartassist.appreciate.be.smartassist.model.api;

import android.content.ContentValues;

import com.google.gson.annotations.SerializedName;

import smartassist.appreciate.be.smartassist.database.PhotoTable;

/**
 * Created by Inneke on 3/03/2015.
 */
public class PhotoItem
{
    private int id;
    private String title;
    private String url;
    @SerializedName("thumbnail_url")
    private String thumbnailUrl;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("deleted_at")
    private String deletedAt;

    public int getId()
    {
        return id;
    }

    public String getCreatedAt()
    {
        return createdAt;
    }

    public String getUpdatedAt()
    {
        return updatedAt;
    }

    public String getDeletedAt()
    {
        return deletedAt;
    }

    public ContentValues getContentValues(long timestamp, boolean screenSaver)
    {
        final ContentValues cv = new ContentValues();

        cv.put(PhotoTable.COLUMN_PHOTO_ID, this.id);
        cv.put(PhotoTable.COLUMN_DESCRIPTION, this.title);
        cv.put(PhotoTable.COLUMN_IMAGE, this.url);
        cv.put(PhotoTable.COLUMN_THUMBNAIL, this.thumbnailUrl);
        cv.put(PhotoTable.COLUMN_SCREEN_SAVER, screenSaver ? 1 : 0);
        cv.put(PhotoTable.COLUMN_LAST_UPDATE, timestamp);

        return cv;
}
}
