package smartassist.appreciate.be.smartassist.model.api;

import android.content.ContentValues;

import com.google.gson.annotations.SerializedName;

import smartassist.appreciate.be.smartassist.database.RssTable;
import smartassist.appreciate.be.smartassist.utils.DateUtils;

/**
 * Created by Inneke on 25/02/2015.
 */
public class RssItem
{
    private int id;
    private String title;
    private String intro;
    private String body;
    private String image;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("deleted_at")
    private String deletedAt;
    @SerializedName("published_at")
    private String publishedAt;

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

    public ContentValues getContentValues(long timestamp)
    {
        final ContentValues cv = new ContentValues();

        cv.put(RssTable.COLUMN_RSS_ID, this.id);
        cv.put(RssTable.COLUMN_TITLE, this.title);
        cv.put(RssTable.COLUMN_INTRO, this.intro);
        cv.put(RssTable.COLUMN_BODY, this.body);
        cv.put(RssTable.COLUMN_IMAGE, this.image);
        cv.put(RssTable.COLUMN_DATE, DateUtils.parseApiDateSeconds(this.publishedAt));
        cv.put(RssTable.COLUMN_LAST_UPDATE, timestamp);

        return cv;
    }
}
