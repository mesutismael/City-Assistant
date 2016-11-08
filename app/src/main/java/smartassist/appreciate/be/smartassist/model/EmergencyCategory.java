package smartassist.appreciate.be.smartassist.model;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import smartassist.appreciate.be.smartassist.database.EmergencyCategoryTable;

/**
 * Created by Inneke De Clippel on 9/09/2016.
 */
public class EmergencyCategory
{
    private int id;
    private String name;

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public static List<EmergencyCategory> constructListFromCursor(Cursor cursor)
    {
        List<EmergencyCategory> categories = new ArrayList<>();

        if(cursor != null && cursor.moveToFirst())
        {
            do
            {
                categories.add(EmergencyCategory.constructFromCursor(cursor));
            }
            while (cursor.moveToNext());
        }

        return categories;
    }

    public static EmergencyCategory constructFromCursor(Cursor cursor)
    {
        EmergencyCategory category = new EmergencyCategory();

        category.id = cursor.getInt(cursor.getColumnIndex(EmergencyCategoryTable.COLUMN_CATEGORY_ID_FULL));
        category.name = cursor.getString(cursor.getColumnIndex(EmergencyCategoryTable.COLUMN_NAME_FULL));

        return category;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        EmergencyCategory that = (EmergencyCategory) o;

        if (this.id != that.id)
        {
            return false;
        }
        return this.name != null ? this.name.equals(that.name) : that.name == null;

    }
}
