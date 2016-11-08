package smartassist.appreciate.be.smartassist.model;

import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import smartassist.appreciate.be.smartassist.database.ActivityTypesTable;
import smartassist.appreciate.be.smartassist.database.EmergencyCategoryTable;
import smartassist.appreciate.be.smartassist.database.PoiCategoryTable;

/**
 * Created by Inneke De Clippel on 2/08/2016.
 */
public class Category
{
    public static final int TYPE_POI = 1;
    public static final int TYPE_EMERGENCY = 2;
    public static final int TYPE_ACTIVITY = 3;
    //TODO: remove this demo image and make appropriate adjustments
    public static final String DEMO_IMAGE="http://test.smartassist.appreciate.be/images/large/1477906484-websitecard_05.png";
    public static final String DEMO_IMAGE_2="http://test.smartassist.appreciate.be/images/large/1477906191-appreciate_logo.png";
    private int id;
    private String name;

    public static List<Category> constructListFromCursor(Cursor cursor, int type)
    {
        List<Category> categories = new ArrayList<>();
        Set<Category> nonDuplicateCategories = new TreeSet<>(new Comparator<Category>() {
            @Override
            public int compare(Category lhs, Category rhs) {

                if(lhs.getName().equalsIgnoreCase(rhs.getName()))
                    return 0;
                else
                    return 1;
            }
        });


        if(cursor != null && cursor.moveToFirst())
        {
            do
            {
                nonDuplicateCategories.add(Category.constructFromCursor(cursor, type));

            }
            while (cursor.moveToNext());
        }

        categories.addAll(nonDuplicateCategories);

        return categories;
    }

    public static Category constructFromCursor(Cursor cursor, int type)
    {
        Category category = new Category();

             switch (type)
             {
                 case TYPE_POI:
                     category.id = cursor.getInt(cursor.getColumnIndex(PoiCategoryTable.COLUMN_CATEGORY_ID_FULL));
                     category.name = cursor.getString(cursor.getColumnIndex(PoiCategoryTable.COLUMN_NAME_FULL));
                     break;

                 case TYPE_EMERGENCY:
                     category.id = cursor.getInt(cursor.getColumnIndex(EmergencyCategoryTable.COLUMN_CATEGORY_ID));
                     category.name = cursor.getString(cursor.getColumnIndex(EmergencyCategoryTable.COLUMN_NAME));
                     break;

                 case TYPE_ACTIVITY:
                     category.id = cursor.getInt(cursor.getColumnIndex(ActivityTypesTable.COLUMN_ID_FULL));
                     category.name = cursor.getString(cursor.getColumnIndex(ActivityTypesTable.COLUMN_NAME_FULL));
                     break;
             }
        Log.d("category.id",""+category.id);
        Log.d("category.name",category.name);


        return category;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

}
