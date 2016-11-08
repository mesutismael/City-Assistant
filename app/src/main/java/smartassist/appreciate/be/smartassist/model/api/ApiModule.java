package smartassist.appreciate.be.smartassist.model.api;

import android.content.ContentValues;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import smartassist.appreciate.be.smartassist.database.ModuleTable;

/**
 * Created by Inneke on 26/01/2015.
 */
public class ApiModule implements Serializable
{
    @SerializedName("module_id")
    private int id;
    @SerializedName("col_portrait")
    private int columnPortrait;
    @SerializedName("row_portrait")
    private int rowPortrait;
    @SerializedName("colspan_portrait")
    private int columnSpanPortrait;
    @SerializedName("rowspan_portrait")
    private int rowSpanPortrait;
    @SerializedName("col_land")
    private int columnLandscape;
    @SerializedName("row_land")
    private int rowLandscape;
    @SerializedName("colspan_land")
    private int columnSpanLandscape;
    @SerializedName("rowspan_land")
    private int rowSpanLandscape;

    public ApiModule(int id, int columnPortrait, int rowPortrait, int columnSpanPortrait, int rowSpanPortrait, int columnLandscape, int rowLandscape, int columnSpanLandscape, int rowSpanLandscape)
    {
        this.id = id;
        this.columnPortrait = columnPortrait;
        this.rowPortrait = rowPortrait;
        this.columnSpanPortrait = columnSpanPortrait;
        this.rowSpanPortrait = rowSpanPortrait;
        this.columnLandscape = columnLandscape;
        this.rowLandscape = rowLandscape;
        this.columnSpanLandscape = columnSpanLandscape;
        this.rowSpanLandscape = rowSpanLandscape;
    }

    public ContentValues getContentValues(long timestamp)
    {
        final ContentValues cv = new ContentValues();

        cv.put(ModuleTable.COLUMN_MODULE_ID, this.id);
        cv.put(ModuleTable.COLUMN_COLUMN_PORTRAIT, this.columnPortrait);
        cv.put(ModuleTable.COLUMN_ROW_PORTRAIT, this.rowPortrait);
        cv.put(ModuleTable.COLUMN_COLUMN_SPAN_PORTRAIT, this.columnSpanPortrait);
        cv.put(ModuleTable.COLUMN_ROW_SPAN_PORTRAIT, this.rowSpanPortrait);
        cv.put(ModuleTable.COLUMN_COLUMN_LAND, this.columnLandscape);
        cv.put(ModuleTable.COLUMN_ROW_LAND, this.rowLandscape);
        cv.put(ModuleTable.COLUMN_COLUMN_SPAN_LAND, this.columnSpanLandscape);
        cv.put(ModuleTable.COLUMN_ROW_SPAN_LAND, this.rowSpanLandscape);
        cv.put(ModuleTable.COLUMN_LAST_UPDATE, timestamp);

        return cv;
    }
}
