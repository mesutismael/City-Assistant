package smartassist.appreciate.be.smartassist.utils;

import android.database.Cursor;
import android.text.TextUtils;

import smartassist.appreciate.be.smartassist.database.ActivityTypesTable;
import smartassist.appreciate.be.smartassist.database.EmergencyCategoryTable;
import smartassist.appreciate.be.smartassist.database.EmergencyTable;
import smartassist.appreciate.be.smartassist.database.NewsCategoryTable;
import smartassist.appreciate.be.smartassist.database.NewsTable;
import smartassist.appreciate.be.smartassist.database.PoiCategoryTable;
import smartassist.appreciate.be.smartassist.database.PoiTable;
import smartassist.appreciate.be.smartassist.model.CategoryPhoto;
import smartassist.appreciate.be.smartassist.model.EmergencyContact;
import smartassist.appreciate.be.smartassist.model.News;
import smartassist.appreciate.be.smartassist.model.Poi;

/**
 * Created by Inneke on 21/04/2015.
 */
public class CategoryHelper
{
    public static String getEmergencyContactPhoto(EmergencyContact contact, boolean large)
    {
        if(!TextUtils.isEmpty(contact.getPhoto()))
            return contact.getPhoto().replaceAll("\\s", "%20");

        double random = contact.getRandomPhoto();
        CategoryPhoto photos = contact.getCategoryPhoto();
        String photo1 = large ? photos.getPhoto1Large() : photos.getPhoto1();
        String photo2 = large ? photos.getPhoto2Large() : photos.getPhoto2();
        String photo3 = large ? photos.getPhoto3Large() : photos.getPhoto3();
        String photo4 = large ? photos.getPhoto4Large() : photos.getPhoto4();
        String photo5 = large ? photos.getPhoto5Large() : photos.getPhoto5();

        return getPhoto(random, photo1, photo2, photo3, photo4, photo5);
    }

    public static String getPoiPhoto(Poi poi, boolean large)
    {
        double random = poi.getRandomPhoto();
        CategoryPhoto photos = poi.getCategoryPhoto();
        String photo1 = large ? photos.getPhoto1Large() : photos.getPhoto1();
        String photo2 = large ? photos.getPhoto2Large() : photos.getPhoto2();
        String photo3 = large ? photos.getPhoto3Large() : photos.getPhoto3();
        String photo4 = large ? photos.getPhoto4Large() : photos.getPhoto4();
        String photo5 = large ? photos.getPhoto5Large() : photos.getPhoto5();

        return getPhoto(random, photo1, photo2, photo3, photo4, photo5);
    }

    public static String getNewsPhoto(News news, boolean large)
    {
        double random = news.getRandomPhoto();
        CategoryPhoto photos = news.getCategoryPhoto();
        String photo1 = large ? photos.getPhoto1Large() : photos.getPhoto1();
        String photo2 = large ? photos.getPhoto2Large() : photos.getPhoto2();
        String photo3 = large ? photos.getPhoto3Large() : photos.getPhoto3();
        String photo4 = large ? photos.getPhoto4Large() : photos.getPhoto4();
        String photo5 = large ? photos.getPhoto5Large() : photos.getPhoto5();

        return getPhoto(random, photo1, photo2, photo3, photo4, photo5);
    }

    public static String getEmergencyPhoto(Cursor cursor, boolean large)
    {
        if(cursor == null)
            return null;

        String photo = cursor.getString(cursor.getColumnIndex(EmergencyTable.COLUMN_PHOTO_FULL));
        if(!TextUtils.isEmpty(photo))
            return photo.replaceAll("\\s", "%20");

        double random = cursor.getDouble(cursor.getColumnIndex(EmergencyTable.COLUMN_RANDOM_PHOTO_FULL));
        String photo1 = cursor.getString(cursor.getColumnIndex(large ? EmergencyCategoryTable.COLUMN_IMAGE_PORTRAIT_1_FULL : EmergencyCategoryTable.COLUMN_IMAGE_LANDSCAPE_1_FULL));
        String photo2 = cursor.getString(cursor.getColumnIndex(large ? EmergencyCategoryTable.COLUMN_IMAGE_PORTRAIT_2_FULL : EmergencyCategoryTable.COLUMN_IMAGE_LANDSCAPE_2_FULL));
        String photo3 = cursor.getString(cursor.getColumnIndex(large ? EmergencyCategoryTable.COLUMN_IMAGE_PORTRAIT_3_FULL : EmergencyCategoryTable.COLUMN_IMAGE_LANDSCAPE_3_FULL));
        String photo4 = cursor.getString(cursor.getColumnIndex(large ? EmergencyCategoryTable.COLUMN_IMAGE_PORTRAIT_4_FULL : EmergencyCategoryTable.COLUMN_IMAGE_LANDSCAPE_4_FULL));
        String photo5 = cursor.getString(cursor.getColumnIndex(large ? EmergencyCategoryTable.COLUMN_IMAGE_PORTRAIT_5_FULL : EmergencyCategoryTable.COLUMN_IMAGE_LANDSCAPE_5_FULL));

        return getPhoto(random, photo1, photo2, photo3, photo4, photo5);
    }

    public static String getPoiPhoto(Cursor cursor, boolean large)
    {
        if(cursor == null)
            return null;

        double random = cursor.getDouble(cursor.getColumnIndex(PoiTable.COLUMN_RANDOM_PHOTO_FULL));
        String photo1 = cursor.getString(cursor.getColumnIndex(large ? PoiCategoryTable.COLUMN_IMAGE_PORTRAIT_1_FULL : PoiCategoryTable.COLUMN_IMAGE_LANDSCAPE_1_FULL));
        String photo2 = cursor.getString(cursor.getColumnIndex(large ? PoiCategoryTable.COLUMN_IMAGE_PORTRAIT_2_FULL : PoiCategoryTable.COLUMN_IMAGE_LANDSCAPE_2_FULL));
        String photo3 = cursor.getString(cursor.getColumnIndex(large ? PoiCategoryTable.COLUMN_IMAGE_PORTRAIT_3_FULL : PoiCategoryTable.COLUMN_IMAGE_LANDSCAPE_3_FULL));
        String photo4 = cursor.getString(cursor.getColumnIndex(large ? PoiCategoryTable.COLUMN_IMAGE_PORTRAIT_4_FULL : PoiCategoryTable.COLUMN_IMAGE_LANDSCAPE_4_FULL));
        String photo5 = cursor.getString(cursor.getColumnIndex(large ? PoiCategoryTable.COLUMN_IMAGE_PORTRAIT_5_FULL : PoiCategoryTable.COLUMN_IMAGE_LANDSCAPE_5_FULL));

        return getPhoto(random, photo1, photo2, photo3, photo4, photo5);
    }

    public static String getActivityPhoto(Cursor cursor, boolean large)
    {
        if(cursor == null)
            return null;

/*        double random = cursor.getDouble(cursor.getColumnIndex(PoiTable.COLUMN_RANDOM_PHOTO_FULL));
        String photo1 = cursor.getString(cursor.getColumnIndex(large ? PoiCategoryTable.COLUMN_IMAGE_PORTRAIT_1_FULL : PoiCategoryTable.COLUMN_IMAGE_LANDSCAPE_1_FULL));
        String photo2 = cursor.getString(cursor.getColumnIndex(large ? PoiCategoryTable.COLUMN_IMAGE_PORTRAIT_2_FULL : PoiCategoryTable.COLUMN_IMAGE_LANDSCAPE_2_FULL));
        String photo3 = cursor.getString(cursor.getColumnIndex(large ? PoiCategoryTable.COLUMN_IMAGE_PORTRAIT_3_FULL : PoiCategoryTable.COLUMN_IMAGE_LANDSCAPE_3_FULL));
        String photo4 = cursor.getString(cursor.getColumnIndex(large ? PoiCategoryTable.COLUMN_IMAGE_PORTRAIT_4_FULL : PoiCategoryTable.COLUMN_IMAGE_LANDSCAPE_4_FULL));*/
        String photo5 = cursor.getString(cursor.getColumnIndex(large ? ActivityTypesTable.COLUMN_LANDSCAPE_IMAGE_FULL : ActivityTypesTable.COLUMN_POTRAIT_IMAGE_FULL));

        return photo5;
    }

    public static String getNewsPhoto(Cursor cursor, boolean large)
    {
        if(cursor == null)
            return null;

        double random = cursor.getDouble(cursor.getColumnIndex(NewsTable.COLUMN_RANDOM_PHOTO_FULL));
        String photo1 = cursor.getString(cursor.getColumnIndex(large ? NewsCategoryTable.COLUMN_IMAGE_PORTRAIT_1_FULL : NewsCategoryTable.COLUMN_IMAGE_LANDSCAPE_1_FULL));
        String photo2 = cursor.getString(cursor.getColumnIndex(large ? NewsCategoryTable.COLUMN_IMAGE_PORTRAIT_2_FULL : NewsCategoryTable.COLUMN_IMAGE_LANDSCAPE_2_FULL));
        String photo3 = cursor.getString(cursor.getColumnIndex(large ? NewsCategoryTable.COLUMN_IMAGE_PORTRAIT_3_FULL : NewsCategoryTable.COLUMN_IMAGE_LANDSCAPE_3_FULL));
        String photo4 = cursor.getString(cursor.getColumnIndex(large ? NewsCategoryTable.COLUMN_IMAGE_PORTRAIT_4_FULL : NewsCategoryTable.COLUMN_IMAGE_LANDSCAPE_4_FULL));
        String photo5 = cursor.getString(cursor.getColumnIndex(large ? NewsCategoryTable.COLUMN_IMAGE_PORTRAIT_5_FULL : NewsCategoryTable.COLUMN_IMAGE_LANDSCAPE_5_FULL));

        return getPhoto(random, photo1, photo2, photo3, photo4, photo5);
    }

    private static String getPhoto(double random, String photo1, String photo2, String photo3, String photo4, String photo5)
    {
        int highestPhotoIndex;
        if (!TextUtils.isEmpty(photo5))
            highestPhotoIndex = 5;
        else if (!TextUtils.isEmpty(photo4))
            highestPhotoIndex = 4;
        else if (!TextUtils.isEmpty(photo3))
            highestPhotoIndex = 3;
        else if (!TextUtils.isEmpty(photo2))
            highestPhotoIndex = 2;
        else
            highestPhotoIndex = 1;

        String randomPhoto;
        int randomInteger = (int) (random * highestPhotoIndex) + 1;
        switch (randomInteger)
        {
            case 1: randomPhoto = photo1; break;
            case 2: randomPhoto = photo2; break;
            case 3: randomPhoto = photo3; break;
            case 4: randomPhoto = photo4; break;
            case 5: randomPhoto = photo5; break;
            default: randomPhoto = photo1;
        }

        if(!TextUtils.isEmpty(randomPhoto))
            randomPhoto = randomPhoto.replaceAll("\\s", "%20");

        return randomPhoto;
    }
}
