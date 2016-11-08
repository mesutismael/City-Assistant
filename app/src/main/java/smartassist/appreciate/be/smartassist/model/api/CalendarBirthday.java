package smartassist.appreciate.be.smartassist.model.api;

import android.content.ContentValues;

import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

import smartassist.appreciate.be.smartassist.database.CalendarBirthdayTable;
import smartassist.appreciate.be.smartassist.utils.DateUtils;

/**
 * Created by Inneke on 20/02/2015.
 */
public class CalendarBirthday
{
    private int id;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    private String birthday;
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
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(DateUtils.parseApiDate(this.birthday));

        final ContentValues cv = new ContentValues();

        cv.put(CalendarBirthdayTable.COLUMN_BIRTHDAY_ID, this.id);
        cv.put(CalendarBirthdayTable.COLUMN_FIRST_NAME, this.firstName);
        cv.put(CalendarBirthdayTable.COLUMN_LAST_NAME, this.lastName);
        cv.put(CalendarBirthdayTable.COLUMN_DAY, calendar.get(Calendar.DAY_OF_MONTH));
        cv.put(CalendarBirthdayTable.COLUMN_MONTH, calendar.get(Calendar.MONTH));
        cv.put(CalendarBirthdayTable.COLUMN_YEAR, calendar.get(Calendar.YEAR));
        cv.put(CalendarBirthdayTable.COLUMN_LAST_UPDATE, timestamp);

        return cv;
    }
}
