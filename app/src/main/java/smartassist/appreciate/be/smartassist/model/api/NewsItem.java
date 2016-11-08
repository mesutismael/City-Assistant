package smartassist.appreciate.be.smartassist.model.api;

import android.content.ContentValues;

import com.google.gson.annotations.SerializedName;

import smartassist.appreciate.be.smartassist.database.NewsTable;
import smartassist.appreciate.be.smartassist.utils.DateUtils;

/**
 * Created by Inneke on 23/02/2015.
 */
public class NewsItem
{
    private int id;
    @SerializedName("news_category_id")
    private int categoryId;
    private String title;
    private String body;
    private String period;
    @SerializedName("start_period")
    private String startDate;
    @SerializedName("end_period")
    private String endDate;
    @SerializedName("hard_reminder")
    private boolean hardReminder;
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

    public String getTitle()
    {
        return title;
    }

    public String getBody()
    {
        return body;
    }

    public boolean isHardReminder()
    {
        return hardReminder;
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

        cv.put(NewsTable.COLUMN_NEWS_ID, this.id);
        cv.put(NewsTable.COLUMN_CATEGORY_ID, this.categoryId);
        cv.put(NewsTable.COLUMN_TITLE, this.title);
        cv.put(NewsTable.COLUMN_BODY, this.body);
        cv.put(NewsTable.COLUMN_PERIOD, this.period);
        cv.put(NewsTable.COLUMN_START_DATE, DateUtils.parseApiDate(this.startDate));
        cv.put(NewsTable.COLUMN_END_DATE, DateUtils.parseApiDate(this.endDate));
        cv.put(NewsTable.COLUMN_CREATION_DATE, DateUtils.parseApiDateSeconds(this.updatedAt));
        cv.put(NewsTable.COLUMN_LAST_UPDATE, timestamp);

        return cv;
    }

    public ContentValues getContentValuesWithRandomPhoto(long timestamp)
    {
        ContentValues cv = this.getContentValues(timestamp);

        cv.put(NewsTable.COLUMN_RANDOM_PHOTO, Math.random());

        return cv;
    }
}
