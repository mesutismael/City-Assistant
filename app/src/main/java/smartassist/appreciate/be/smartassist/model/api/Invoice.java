package smartassist.appreciate.be.smartassist.model.api;

import android.content.ContentValues;

import com.google.gson.annotations.SerializedName;

import smartassist.appreciate.be.smartassist.database.InvoiceTable;

/**
 * Created by Inneke De Clippel on 1/08/2016.
 */
public class Invoice
{
    private int id;
    private String number;
    @SerializedName("period")
    private String date;
    @SerializedName("file")
    private String url;
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

        cv.put(InvoiceTable.COLUMN_INVOICE_ID, this.id);
        cv.put(InvoiceTable.COLUMN_NUMBER, this.number);
        cv.put(InvoiceTable.COLUMN_DATE, this.date);
        cv.put(InvoiceTable.COLUMN_URL, this.url);
        cv.put(InvoiceTable.COLUMN_LAST_UPDATE, timestamp);

        return cv;
    }
}
