package smartassist.appreciate.be.smartassist.model.api;

import android.content.ContentValues;

import com.google.gson.annotations.SerializedName;

import smartassist.appreciate.be.smartassist.database.ActivityTable;

/**
 * Created by Banada ismael on 10/31/2016.
 */

public class ActivityItem {
    @SerializedName("id")
    private int id;
    @SerializedName("activity_type_id")
    private int activityTypeId;
    @SerializedName("residence_id")
    private int residenceId;
    @SerializedName("photo")
    private String photo;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("location")
    private String location;
    @SerializedName("start_date")
    private String startDate;
    @SerializedName("end_date")
    private String endDate;
    @SerializedName("start_time")
    private String startTime;
    @SerializedName("end_time")
    private String endTime;
    @SerializedName("invoice_code")
    private String invoiceCode;
    @SerializedName("invoice_description")
    private String invoiceDescription;
    @SerializedName("invoice_vat")
    private String invoiceVat;
    @SerializedName("invoice_price")
    private String invoicePrice;
    @SerializedName("invoice_date")
    private String invoiceDate;
    @SerializedName("invoiced")
    private String invoiced;
    @SerializedName("repeat_code")
    private int repeatCode;
    @SerializedName("repeat_amount")
    private int repeatAmount;
    @SerializedName("repeat_end_date")
    private String repeatEndDate;
    @SerializedName("agent_item_id")
    private int agendaitemId;
    @SerializedName("news_item_id")
    private int newsItemId;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("deleted_at")
    private String deletedAt;


    public int getId() {return id;}

    public int getActivityTypeId() {return activityTypeId;}

    public int getResidenceId() {return residenceId;}

    public String getPhoto() {return photo;}

    public String getStartDate() {return startDate;}

    public String getEndDate() {return endDate;}

    public String getStartTime() {return startTime;}

    public String getEndTime() {return endTime;}

    public int getNewsItemId() {return newsItemId;}

    public String getCreatedAt() {return createdAt;}

    public String getUpdatedAt() {return updatedAt;}

    public String getDeletedAt() {return deletedAt;}

    public ContentValues getContentValues(long timestamp)
    {
        final ContentValues cv = new ContentValues();
        cv.put(ActivityTable.COLUMN_ID, this.id);
        cv.put(ActivityTable.COLUMN_ACTIVITY_TYPE_ID, this.activityTypeId);
        cv.put(ActivityTable.COLUMN_RESIDENCE_ID, this.residenceId);
        cv.put(ActivityTable.COLUMN_PHOTO, this.photo);
        cv.put(ActivityTable.COLUMN_NAME, this.name);
        cv.put(ActivityTable.COLUMN_DESCRIPTION, this.description);
        cv.put(ActivityTable.COLUMN_LOCATION, this.location);
        cv.put(ActivityTable.COLUMN_START_DATE, this.startDate);
        cv.put(ActivityTable.COLUMN_END_DATE, this.endDate);
        cv.put(ActivityTable.COLUMN_START_TIME, this.startTime);
        cv.put(ActivityTable.COLUMN_END_TIME, this.endTime);
        cv.put(ActivityTable.COLUMN_INVOICE_CODE, this.invoiceCode);
        cv.put(ActivityTable.COLUMN_INVOICE_DESCRIPTION, this.invoiceDescription);
        cv.put(ActivityTable.COLUMN_INVOICE_DATE, this.invoiceDate);
        cv.put(ActivityTable.COLUMN_INVOICE_PRICE, this.invoicePrice);
        cv.put(ActivityTable.COLUMN_INVOICE_VAT, this.invoiceVat);
        cv.put(ActivityTable.COLUMN_INVOICED, this.invoiced);
        cv.put(ActivityTable.COLUMN_REPEAT_CODE, this.repeatCode);
        cv.put(ActivityTable.COLUMN_REPEAT_AMOUNT, this.repeatAmount);
        cv.put(ActivityTable.COLUMN_REPEAT_END_DATE, this.repeatEndDate);
        cv.put(ActivityTable.COLUMN_AGENT_ITEM_ID, this.agendaitemId);
        cv.put(ActivityTable.COLUMN_NEWS_ITEM_ID, this.newsItemId);
        cv.put(ActivityTable.COLUMN_CREATED_AT, this.createdAt);
        cv.put(ActivityTable.COLUMN_UPDATED_AT, timestamp);
        cv.put(ActivityTable.COLUMN_DELETED_AT, this.deletedAt);

        return cv;
    }



}
