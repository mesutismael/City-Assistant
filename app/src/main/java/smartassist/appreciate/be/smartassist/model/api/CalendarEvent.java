package smartassist.appreciate.be.smartassist.model.api;

import android.content.ContentValues;

import com.google.gson.annotations.SerializedName;

import smartassist.appreciate.be.smartassist.database.CalendarEventTable;
import smartassist.appreciate.be.smartassist.utils.CalendarUtils;
import smartassist.appreciate.be.smartassist.utils.DateUtils;

/**
 * Created by Inneke on 20/02/2015.
 */
public class CalendarEvent
{
    private int id;
    private String description;
    @SerializedName("start_date")
    private String startDate;
    @SerializedName("end_date")
    private String stopDate;
    @SerializedName("start_time")
    private String startTime;
    @SerializedName("end_time")
    private String stopTime;
    private int level;
    private int reminder;
    @SerializedName("hard_reminder")
    private boolean hardReminder;
    @SerializedName("instant_reminder")
    private boolean instantReminder;
    @SerializedName("repeat_code")
    private int repeatCode;
    @SerializedName("repeat_amount")
    private int repeatAmount;
    @SerializedName("repeat_end_date")
    private String repeatUntil;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("deleted_at")
    private String deletedAt;

    public static final int LEVEL_HABITANT_1 = 1;
    public static final int LEVEL_HABITANT_2 = 2;
    public static final int LEVEL_FLAT = 3;

    public int getId()
    {
        return id;
    }

    public String getDescription()
    {
        return description;
    }

    public boolean isHardReminder()
    {
        return hardReminder;
    }

    public boolean isInstantReminder()
    {
        return instantReminder;
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

    public ContentValues getContentValues(long timestamp, int level)
    {
        long start = DateUtils.parseApiDateMinutes(this.startDate + " " + this.startTime);
        long stop = DateUtils.parseApiDateMinutes(this.stopDate + " " + this.stopTime);
        long repeatUntil = DateUtils.parseApiDateMinutes(this.repeatUntil + " 23:59");

        ContentValues cv = new ContentValues();

        cv.put(CalendarEventTable.COLUMN_EVENT_ID, this.id);
        cv.put(CalendarEventTable.COLUMN_DESCRIPTION, this.description);
        cv.put(CalendarEventTable.COLUMN_START, start);
        cv.put(CalendarEventTable.COLUMN_STOP, stop);
        cv.put(CalendarEventTable.COLUMN_ALARM, CalendarUtils.getAlarmTime(start, this.reminder));
        cv.put(CalendarEventTable.COLUMN_ALARM_FINISHED, 0);
        cv.put(CalendarEventTable.COLUMN_LEVEL, level);
        cv.put(CalendarEventTable.COLUMN_LAST_UPDATE, timestamp);
        cv.put(CalendarEventTable.COLUMN_REPEAT_CODE, this.repeatCode);
        cv.put(CalendarEventTable.COLUMN_REPEAT_AMOUNT, this.repeatAmount);
        cv.put(CalendarEventTable.COLUMN_REPEAT_UNTIL, repeatUntil);

        return cv;
    }
}
