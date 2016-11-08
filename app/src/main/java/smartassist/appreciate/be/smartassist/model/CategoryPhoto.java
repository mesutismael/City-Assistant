package smartassist.appreciate.be.smartassist.model;

import android.database.Cursor;

import smartassist.appreciate.be.smartassist.database.EmergencyCategoryTable;
import smartassist.appreciate.be.smartassist.database.NewsCategoryTable;
import smartassist.appreciate.be.smartassist.database.PoiCategoryTable;

/**
 * Created by Inneke De Clippel on 2/08/2016.
 */
public class CategoryPhoto
{
    private String photo1;
    private String photo1Large;
    private String photo2;
    private String photo2Large;
    private String photo3;
    private String photo3Large;
    private String photo4;
    private String photo4Large;
    private String photo5;
    private String photo5Large;

    public static final int TYPE_POI = 1;
    public static final int TYPE_EMERGENCY = 2;
    public static final int TYPE_NEWS = 3;

    public String getPhoto1()
    {
        return photo1;
    }

    public String getPhoto1Large()
    {
        return photo1Large;
    }

    public String getPhoto2()
    {
        return photo2;
    }

    public String getPhoto2Large()
    {
        return photo2Large;
    }

    public String getPhoto3()
    {
        return photo3;
    }

    public String getPhoto3Large()
    {
        return photo3Large;
    }

    public String getPhoto4()
    {
        return photo4;
    }

    public String getPhoto4Large()
    {
        return photo4Large;
    }

    public String getPhoto5()
    {
        return photo5;
    }

    public String getPhoto5Large()
    {
        return photo5Large;
    }

    public static CategoryPhoto constructFromCursor(Cursor cursor, int type)
    {
        CategoryPhoto photo = new CategoryPhoto();

        switch (type)
        {
            case TYPE_POI:
                photo.photo1 = cursor.getString(cursor.getColumnIndex(PoiCategoryTable.COLUMN_IMAGE_LANDSCAPE_1_FULL));
                photo.photo1Large = cursor.getString(cursor.getColumnIndex(PoiCategoryTable.COLUMN_IMAGE_PORTRAIT_1_FULL));
                photo.photo2 = cursor.getString(cursor.getColumnIndex(PoiCategoryTable.COLUMN_IMAGE_LANDSCAPE_2_FULL));
                photo.photo2Large = cursor.getString(cursor.getColumnIndex(PoiCategoryTable.COLUMN_IMAGE_PORTRAIT_2_FULL));
                photo.photo3 = cursor.getString(cursor.getColumnIndex(PoiCategoryTable.COLUMN_IMAGE_LANDSCAPE_3_FULL));
                photo.photo3Large = cursor.getString(cursor.getColumnIndex(PoiCategoryTable.COLUMN_IMAGE_PORTRAIT_3_FULL));
                photo.photo4 = cursor.getString(cursor.getColumnIndex(PoiCategoryTable.COLUMN_IMAGE_LANDSCAPE_4_FULL));
                photo.photo4Large = cursor.getString(cursor.getColumnIndex(PoiCategoryTable.COLUMN_IMAGE_PORTRAIT_4_FULL));
                photo.photo5 = cursor.getString(cursor.getColumnIndex(PoiCategoryTable.COLUMN_IMAGE_LANDSCAPE_5_FULL));
                photo.photo5Large = cursor.getString(cursor.getColumnIndex(PoiCategoryTable.COLUMN_IMAGE_PORTRAIT_5_FULL));
                break;

            case TYPE_EMERGENCY:
                photo.photo1 = cursor.getString(cursor.getColumnIndex(EmergencyCategoryTable.COLUMN_IMAGE_LANDSCAPE_1_FULL));
                photo.photo1Large = cursor.getString(cursor.getColumnIndex(EmergencyCategoryTable.COLUMN_IMAGE_PORTRAIT_1_FULL));
                photo.photo2 = cursor.getString(cursor.getColumnIndex(EmergencyCategoryTable.COLUMN_IMAGE_LANDSCAPE_2_FULL));
                photo.photo2Large = cursor.getString(cursor.getColumnIndex(EmergencyCategoryTable.COLUMN_IMAGE_PORTRAIT_2_FULL));
                photo.photo3 = cursor.getString(cursor.getColumnIndex(EmergencyCategoryTable.COLUMN_IMAGE_LANDSCAPE_3_FULL));
                photo.photo3Large = cursor.getString(cursor.getColumnIndex(EmergencyCategoryTable.COLUMN_IMAGE_PORTRAIT_3_FULL));
                photo.photo4 = cursor.getString(cursor.getColumnIndex(EmergencyCategoryTable.COLUMN_IMAGE_LANDSCAPE_4_FULL));
                photo.photo4Large = cursor.getString(cursor.getColumnIndex(EmergencyCategoryTable.COLUMN_IMAGE_PORTRAIT_4_FULL));
                photo.photo5 = cursor.getString(cursor.getColumnIndex(EmergencyCategoryTable.COLUMN_IMAGE_LANDSCAPE_5_FULL));
                photo.photo5Large = cursor.getString(cursor.getColumnIndex(EmergencyCategoryTable.COLUMN_IMAGE_PORTRAIT_5_FULL));
                break;

            case TYPE_NEWS:
                photo.photo1 = cursor.getString(cursor.getColumnIndex(NewsCategoryTable.COLUMN_IMAGE_LANDSCAPE_1_FULL));
                photo.photo1Large = cursor.getString(cursor.getColumnIndex(NewsCategoryTable.COLUMN_IMAGE_PORTRAIT_1_FULL));
                photo.photo2 = cursor.getString(cursor.getColumnIndex(NewsCategoryTable.COLUMN_IMAGE_LANDSCAPE_2_FULL));
                photo.photo2Large = cursor.getString(cursor.getColumnIndex(NewsCategoryTable.COLUMN_IMAGE_PORTRAIT_2_FULL));
                photo.photo3 = cursor.getString(cursor.getColumnIndex(NewsCategoryTable.COLUMN_IMAGE_LANDSCAPE_3_FULL));
                photo.photo3Large = cursor.getString(cursor.getColumnIndex(NewsCategoryTable.COLUMN_IMAGE_PORTRAIT_3_FULL));
                photo.photo4 = cursor.getString(cursor.getColumnIndex(NewsCategoryTable.COLUMN_IMAGE_LANDSCAPE_4_FULL));
                photo.photo4Large = cursor.getString(cursor.getColumnIndex(NewsCategoryTable.COLUMN_IMAGE_PORTRAIT_4_FULL));
                photo.photo5 = cursor.getString(cursor.getColumnIndex(NewsCategoryTable.COLUMN_IMAGE_LANDSCAPE_5_FULL));
                photo.photo5Large = cursor.getString(cursor.getColumnIndex(NewsCategoryTable.COLUMN_IMAGE_PORTRAIT_5_FULL));
                break;
        }

        return photo;
    }
}
