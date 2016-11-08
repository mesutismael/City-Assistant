package smartassist.appreciate.be.smartassist.model.api;

import android.content.ContentValues;

import com.google.gson.annotations.SerializedName;

import smartassist.appreciate.be.smartassist.database.CareBookTable;
import smartassist.appreciate.be.smartassist.utils.DateUtils;

/**
 * Created by Inneke De Clippel on 15/02/2016.
 */
public class HealthCareBookItem
{
    private int id;
    @SerializedName("condition")
    private int smiley;
    private String text;
    private User user;
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

    public ContentValues getContentValues(long timestamp)
    {
        final ContentValues cv = new ContentValues();

        cv.put(CareBookTable.COLUMN_CAREBOOK_ID, this.id);
        cv.put(CareBookTable.COLUMN_SMILEY, this.smiley);
        cv.put(CareBookTable.COLUMN_MESSAGE, this.text);
        cv.put(CareBookTable.COLUMN_AUTHOR, this.user != null ? this.user.getName() : null);
        cv.put(CareBookTable.COLUMN_CREATED_AT, DateUtils.parseApiDateSeconds(this.createdAt));
        cv.put(CareBookTable.COLUMN_LAST_UPDATE, timestamp);

        return cv;
    }
}
