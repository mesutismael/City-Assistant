package smartassist.appreciate.be.smartassist.model;

import android.database.Cursor;

import java.io.Serializable;

import smartassist.appreciate.be.smartassist.database.ModuleTable;

/**
 * Created by Inneke De Clippel on 2/08/2016.
 */
public class LayoutModule implements Serializable
{
    private int id;
    private int columnPortrait;
    private int rowPortrait;
    private int columnSpanPortrait;
    private int rowSpanPortrait;
    private int columnLandscape;
    private int rowLandscape;
    private int columnSpanLandscape;
    private int rowSpanLandscape;

    public int getColumnPortrait()
    {
        return columnPortrait;
    }

    public int getRowPortrait()
    {
        return rowPortrait;
    }

    public int getColumnSpanPortrait()
    {
        return columnSpanPortrait;
    }

    public int getRowSpanPortrait()
    {
        return rowSpanPortrait;
    }

    public int getId()
    {
        return id;
    }

    public int getColumnLandscape()
    {
        return columnLandscape;
    }

    public int getRowLandscape()
    {
        return rowLandscape;
    }

    public int getColumnSpanLandscape()
    {
        return columnSpanLandscape;
    }

    public int getRowSpanLandscape()
    {
        return rowSpanLandscape;
    }

    public static LayoutModule constructFromCursor(Cursor cursor)
    {
        LayoutModule module = new LayoutModule();

        module.id = cursor.getInt(cursor.getColumnIndex(ModuleTable.COLUMN_MODULE_ID));
        module.rowPortrait = cursor.getInt(cursor.getColumnIndex(ModuleTable.COLUMN_ROW_PORTRAIT));
        module.columnPortrait = cursor.getInt(cursor.getColumnIndex(ModuleTable.COLUMN_COLUMN_PORTRAIT));
        module.rowSpanPortrait = cursor.getInt(cursor.getColumnIndex(ModuleTable.COLUMN_ROW_SPAN_PORTRAIT));
        module.columnSpanPortrait = cursor.getInt(cursor.getColumnIndex(ModuleTable.COLUMN_COLUMN_SPAN_PORTRAIT));
        module.rowLandscape = cursor.getInt(cursor.getColumnIndex(ModuleTable.COLUMN_ROW_LAND));
        module.columnLandscape = cursor.getInt(cursor.getColumnIndex(ModuleTable.COLUMN_COLUMN_LAND));
        module.rowSpanLandscape = cursor.getInt(cursor.getColumnIndex(ModuleTable.COLUMN_ROW_SPAN_LAND));
        module.columnSpanLandscape = cursor.getInt(cursor.getColumnIndex(ModuleTable.COLUMN_COLUMN_SPAN_LAND));

        return module;
    }
}
