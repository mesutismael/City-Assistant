package smartassist.appreciate.be.smartassist.model;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import smartassist.appreciate.be.smartassist.database.ActivityTable;
import smartassist.appreciate.be.smartassist.database.ActivityTypesTable;


/**
 * Created by banada ismael on 11/1/2016.
 */

public class ActivityItem {

    private int id;
    private int activityTypeId;
    private int residenceId;
    private String photo;
    private String name;
    private String description;
    private String location;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private int invoiceCode;
    private String invoiceDescription;
    private String invoiceVat;
    private String invoicePrice;
    private String invoiceDate;
    private String invoiced;
    private int repeatCode;
    private int repeatAmount;
    private String repeatEndDate;
    private int agendaitemId;
    private int newsItemId;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;
    private String activityTypePhoto;

    public static List<ActivityItem> constructListFromCursor(Cursor cursor)
    {
        List<ActivityItem> ActivityItems = new ArrayList<>();

        if(cursor != null && cursor.moveToFirst())
        {
            do
            {
                ActivityItems.add(ActivityItem.constructFromCursor(cursor));
            }
            while (cursor.moveToNext());
        }

        return ActivityItems;
    }

    public static ActivityItem constructFromCursor(Cursor cursor)
    {
        ActivityItem activityItem = new ActivityItem();

        activityItem.id = cursor.getInt(cursor.getColumnIndex(ActivityTable.COLUMN_ID_FULL));
        activityItem.name = cursor.getString(cursor.getColumnIndex(ActivityTable.COLUMN_NAME_FULL));
        activityItem.photo = cursor.getString(cursor.getColumnIndex(ActivityTable.COLUMN_PHOTO_FULL));
        activityItem.description = cursor.getString(cursor.getColumnIndex(ActivityTable.COLUMN_NAME_FULL));
        activityItem.activityTypeId = cursor.getInt(cursor.getColumnIndex(ActivityTable.COLUMN_ACTIVITY_TYPE_ID_FULL));
        activityItem.createdAt = cursor.getString(cursor.getColumnIndex(ActivityTable.COLUMN_CREATED_AT_FULL));
        activityItem.newsItemId = cursor.getInt(cursor.getColumnIndex(ActivityTable.COLUMN_NEWS_ITEM_ID_FULL));
        activityItem.location = cursor.getString(cursor.getColumnIndex(ActivityTable.COLUMN_LOCATION_FULL));
        activityItem.invoicePrice = cursor.getString(cursor.getColumnIndex(ActivityTable.COLUMN_INVOICE_PRICE_FULL));
        activityItem.repeatAmount = cursor.getInt(cursor.getColumnIndex(ActivityTable.COLUMN_REPEAT_AMOUNT_FULL));
        activityItem.invoiceCode = cursor.getInt(cursor.getColumnIndex(ActivityTable.COLUMN_INVOICE_CODE_FULL));
        activityItem.startDate = cursor.getString(cursor.getColumnIndex(ActivityTable.COLUMN_START_DATE_FULL));
        activityItem.residenceId = cursor.getInt(cursor.getColumnIndex(ActivityTable.COLUMN_RESIDENCE_ID_FULL));
        activityItem.activityTypePhoto=cursor.getString(cursor.getColumnIndex(ActivityTypesTable.COLUMN_LANDSCAPE_IMAGE_FULL));


        return activityItem;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public int getId() {
        return id;
    }

    public int getActivityTypeId() {
        return activityTypeId;
    }

    public int getResidenceId() {
        return residenceId;
    }

    public String getPhoto() {
        return photo;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public int getInvoiceCode() {
        return invoiceCode;
    }

    public String getInvoiceDescription() {
        return invoiceDescription;
    }

    public int getRepeatAmount() {
        return repeatAmount;
    }

    public int getAgendaitemId() {
        return agendaitemId;
    }

    public String getRepeatEndDate() {
        return repeatEndDate;
    }

    public String getActivityTypePhoto() {return activityTypePhoto;}
}
