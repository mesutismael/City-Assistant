package smartassist.appreciate.be.smartassist.model.api;

import android.content.ContentValues;

import com.google.gson.annotations.SerializedName;

import smartassist.appreciate.be.smartassist.database.OpeningTimesTable;

/**
 * Created by Inneke on 10/02/2015.
 */
public class OpeningTime
{
    private int id;
    @SerializedName("poi_data_id")
    private int poiId;
    private int day;
    @SerializedName("start_am")
    private String startMorning;
    @SerializedName("stop_am")
    private String stopMorning;
    @SerializedName("start_pm")
    private String startMidday;
    @SerializedName("stop_pm")
    private String stopMidday;
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

        cv.put(OpeningTimesTable.COLUMN_OPENING_TIME_ID, this.id);
        cv.put(OpeningTimesTable.COLUMN_POI_ID, this.poiId);
        cv.put(OpeningTimesTable.COLUMN_DAY, this.day);
        cv.put(OpeningTimesTable.COLUMN_START_MORNING, this.startMorning);
        cv.put(OpeningTimesTable.COLUMN_STOP_MORNING, this.stopMorning);
        cv.put(OpeningTimesTable.COLUMN_START_MIDDAY, this.startMidday);
        cv.put(OpeningTimesTable.COLUMN_STOP_MIDDAY, this.stopMidday);
        cv.put(OpeningTimesTable.COLUMN_LAST_UPDATE, timestamp);

        return cv;
    }
}
