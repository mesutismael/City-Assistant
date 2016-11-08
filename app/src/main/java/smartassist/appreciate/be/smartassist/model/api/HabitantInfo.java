package smartassist.appreciate.be.smartassist.model.api;

import android.content.ContentValues;

import com.google.gson.annotations.SerializedName;

import smartassist.appreciate.be.smartassist.database.HabitantTable;
import smartassist.appreciate.be.smartassist.utils.DateUtils;

/**
 * Created by Inneke De Clippel on 4/08/2016.
 */
public class HabitantInfo
{
    @SerializedName("habitant_id")
    private int id;
    @SerializedName("habitant_photo")
    private String photo;
    @SerializedName("habitant_name")
    private String name;
/*    @SerializedName("habitant_flat_id")
    private String flat_id;*/
    @SerializedName("habitant_birthday")
    private String birthday;
    @SerializedName("habitant_national_number")
    private String insuranceNumber;
    @SerializedName("habitant_civil_state")
    private String civilState;
    @SerializedName("habitant_partner")
    private String partner;
    @SerializedName("habitant_phone")
    private String phone;
    @SerializedName("habitant_email")
    private String email;
    @SerializedName("habitant_start")
    private String start;
    @SerializedName("habitant_stop")
    private String stop;
    @SerializedName("habitant_stop_reason")
    private String stopReason;
    @SerializedName("habitant_stop_date")
    private String stopDate;
    @SerializedName("medical_card_mutuality")
    private String mutuality;
    @SerializedName("medical_card_length")
    private String length;
    @SerializedName("medical_card_weight")
    private String weight;
    @SerializedName("medical_card_katz")
    private String katz;
    @SerializedName("medical_card_bel")
    private String bel;
    @SerializedName("medical_card_blood_type")
    private String bloodType;
    @SerializedName("medical_card_doctor")
    private String doctor;
    @SerializedName("medical_card_nurse")
    private String nurse;
    @SerializedName("medical_card_intolerances")
    private String intolerances;
    @SerializedName("medical_card_allergies")
    private String allergies;
    @SerializedName("medical_card_diseases")
    private String diseases;
    @SerializedName("medical_card_aid_devices")
    private String aids;
    @SerializedName("medical_card_remarks")
    private String remarks;
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

        cv.put(HabitantTable.COLUMN_HABITANT_ID, this.id);
        cv.put(HabitantTable.COLUMN_PHOTO, this.photo);
        cv.put(HabitantTable.COLUMN_NAME, this.name);
        cv.put(HabitantTable.COLUMN_FLAT_NUMBER, this.id);
        cv.put(HabitantTable.COLUMN_BIRTHDAY, DateUtils.parseApiDate(this.birthday));
        cv.put(HabitantTable.COLUMN_INSURANCE_NUMBER, this.insuranceNumber);
        cv.put(HabitantTable.COLUMN_CIVIL_STATE, this.civilState);
        cv.put(HabitantTable.COLUMN_PARTNER, this.partner);
        cv.put(HabitantTable.COLUMN_PHONE, this.phone);
        cv.put(HabitantTable.COLUMN_EMAIL, this.email);
        cv.put(HabitantTable.COLUMN_START, DateUtils.parseApiDate(this.start));
        cv.put(HabitantTable.COLUMN_MUTUALITY, this.mutuality);
        cv.put(HabitantTable.COLUMN_LENGTH, this.length);
        cv.put(HabitantTable.COLUMN_WEIGHT, this.weight);
        cv.put(HabitantTable.COLUMN_KATZ, this.katz);
        cv.put(HabitantTable.COLUMN_BEL, this.bel);
        cv.put(HabitantTable.COLUMN_BLOOD_TYPE, this.bloodType);
        cv.put(HabitantTable.COLUMN_DOCTOR, this.doctor);
        cv.put(HabitantTable.COLUMN_NURSE, this.nurse);
        cv.put(HabitantTable.COLUMN_INTOLERANCES, this.intolerances);
        cv.put(HabitantTable.COLUMN_ALLERGIES, this.allergies);
        cv.put(HabitantTable.COLUMN_DISEASES, this.diseases);
        cv.put(HabitantTable.COLUMN_AIDS, this.aids);
        cv.put(HabitantTable.COLUMN_REMARKS, this.remarks);
        cv.put(HabitantTable.COLUMN_LAST_UPDATE, timestamp);

        return cv;
    }
}
