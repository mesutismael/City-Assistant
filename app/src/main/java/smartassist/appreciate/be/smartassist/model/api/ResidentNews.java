package smartassist.appreciate.be.smartassist.model.api;

import android.content.ContentValues;

import com.google.gson.annotations.SerializedName;

import smartassist.appreciate.be.smartassist.database.NewsTable;
import smartassist.appreciate.be.smartassist.database.ResidentNewsTable;
import smartassist.appreciate.be.smartassist.utils.DateUtils;

/**
 * Created by  banada ismael on 10/21/2016.
 */

public class ResidentNews
{
    @SerializedName("id")
    private int id;
    @SerializedName("residence_id")
    private int residence_id;
    @SerializedName("title")
    private String title;
    @SerializedName("body")
    private String body;
    @SerializedName("file")
    private  String file;
    @SerializedName("hard_reminder")
    private Boolean hard_reminder;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private  String updatedAt;
    @SerializedName("deleted_at")
    private String deletedAt;

    public int getId()
    {
        return id;
    }

    public int getResidence_id() {
        return residence_id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getFile() {
        return file;
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

    public Boolean isHardReminder() {return hard_reminder;}

    public ContentValues getContentValues(long timestamp)
    {
        final ContentValues cv = new ContentValues();

        cv.put(ResidentNewsTable.COLUMN_ID, this.id);
        cv.put(ResidentNewsTable.COLUMN_RESIDENCE_ID, this.residence_id);
        cv.put(ResidentNewsTable.COLUMN_TITLE, this.title);
        cv.put(ResidentNewsTable.COLUMN_BODY, this.body);
        cv.put(ResidentNewsTable.COLUMN_FILE, this.file);
        cv.put(ResidentNewsTable.COLUMN_HARD_REMINDER, this.hard_reminder);
        cv.put(ResidentNewsTable.COLUMN_CREATION_DATE, DateUtils.parseApiDateSeconds(this.updatedAt));
        cv.put(ResidentNewsTable.COLUMN_LAST_UPDATE, timestamp);
        cv.put(ResidentNewsTable.COLUMN_DELETION_DATE, DateUtils.parseApiDateSeconds(this.deletedAt));
        return cv;

    }



}
