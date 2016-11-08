package smartassist.appreciate.be.smartassist.model.api;

import android.content.ContentValues;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

import smartassist.appreciate.be.smartassist.database.MedicationTable;
import smartassist.appreciate.be.smartassist.model.Medication;
import smartassist.appreciate.be.smartassist.utils.DateUtils;

/**
 * Created by Inneke De Clippel on 26/04/2016.
 */
public class MedicationItem
{
    private int id;
    @SerializedName("habitant_id")
    private int habitantId;
    private String name;
    @SerializedName("start_date")
    private String startDate;
    @SerializedName("end_date")
    private String endDate;
    @SerializedName("quantity")
    private String dose;
    private int[] moments;
    private int[] days;
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
        int time1 = Medication.TIME_NONE;
        int time2 = Medication.TIME_NONE;
        int time3 = Medication.TIME_NONE;
        int time4 = Medication.TIME_NONE;
        int time5 = Medication.TIME_NONE;
        int time6 = Medication.TIME_NONE;
        int time7 = Medication.TIME_NONE;

        if(this.moments != null)
        {
            for(int moment : this.moments)
            {
                switch (moment)
                {
                    case 1: time1 = Medication.TIME_BEFORE; break;
                    case 2: time1 = Medication.TIME_DURING; break;
                    case 3: time1 = Medication.TIME_AFTER; break;
                    case 4: time2 = Medication.TIME_DURING; break;
                    case 5: time3 = Medication.TIME_BEFORE; break;
                    case 6: time3 = Medication.TIME_DURING; break;
                    case 7: time3 = Medication.TIME_AFTER; break;
                    case 8: time4 = Medication.TIME_DURING; break;
                    case 9: time5 = Medication.TIME_BEFORE; break;
                    case 10: time5 = Medication.TIME_DURING; break;
                    case 11: time5 = Medication.TIME_AFTER; break;
                    case 12: time6 = Medication.TIME_DURING; break;
                    case 13: time7 = Medication.TIME_DURING; break;
                }
            }
        }

        ContentValues cv = new ContentValues();
        cv.put(MedicationTable.COLUMN_MEDICATION_ID, this.id);
        cv.put(MedicationTable.COLUMN_HABITANT_ID, this.habitantId);
        cv.put(MedicationTable.COLUMN_NAME, this.name);
        cv.put(MedicationTable.COLUMN_START_DATE, DateUtils.parseApiDateMedication(this.startDate));
        cv.put(MedicationTable.COLUMN_END_DATE, DateUtils.getEndOfDay(DateUtils.parseApiDateMedication(this.endDate)));
        cv.put(MedicationTable.COLUMN_DOSE, this.dose);
        cv.put(MedicationTable.COLUMN_TIME_1, time1);
        cv.put(MedicationTable.COLUMN_TIME_2, time2);
        cv.put(MedicationTable.COLUMN_TIME_3, time3);
        cv.put(MedicationTable.COLUMN_TIME_4, time4);
        cv.put(MedicationTable.COLUMN_TIME_5, time5);
        cv.put(MedicationTable.COLUMN_TIME_6, time6);
        cv.put(MedicationTable.COLUMN_TIME_7, time7);
        cv.put(MedicationTable.COLUMN_DAYS, Arrays.toString(this.days));
        cv.put(MedicationTable.COLUMN_LAST_UPDATE, timestamp);

        return cv;
    }
}
